import React, { createContext, useCallback, useContext, useEffect, useState } from 'react';
import { loginDemoSession, loginZaloToken } from '../services/apiClient';
import { acquireZaloAccessToken, resolveConfiguredAuthMode } from '../services/zaloSdkAdapter';
import type {
  AppRole,
  AuthMode,
  ParentScreenTab,
  StudentSummaryDto,
  UserProfileDto,
} from '../types/api';

interface AppContextValue {
  token: string | null;
  authMode: AuthMode;
  currentUser: UserProfileDto | null;
  linkedChildren: StudentSummaryDto[];
  selectedStudentId: number;
  selectedStudent: StudentSummaryDto | null;
  activeTab: ParentScreenTab;
  isInitializing: boolean;
  authError: string | null;
  switchRole: (targetRole: AppRole) => Promise<void>;
  selectStudent: (studentId: number) => void;
  navigateTab: (tab: ParentScreenTab) => void;
  retryAuth: () => Promise<void>;
}

const AppContext = createContext<AppContextValue | undefined>(undefined);

export function AppProvider({ children }: { children: React.ReactNode }) {
  const [token, setToken] = useState<string | null>(null);
  const [authMode, setAuthMode] = useState<AuthMode>(resolveConfiguredAuthMode());
  const [currentUser, setCurrentUser] = useState<UserProfileDto | null>(null);
  const [linkedChildren, setLinkedChildren] = useState<StudentSummaryDto[]>([]);
  const [selectedStudentId, setSelectedStudentId] = useState<number>(1);
  const [activeTab, setActiveTab] = useState<ParentScreenTab>('HOME');
  const [isInitializing, setIsInitializing] = useState<boolean>(true);
  const [authError, setAuthError] = useState<string | null>(null);

  const initializeRoleSession = useCallback(async (role: AppRole) => {
    setIsInitializing(true);
    setAuthError(null);
    try {
      const configuredMode = resolveConfiguredAuthMode();
      if (configuredMode === 'ZALO_AUTH' && role === 'PARENT') {
        const zaloToken = await acquireZaloAccessToken();
        if (zaloToken) {
          const zaloRes = await loginZaloToken(zaloToken);
          setToken(zaloRes.data.accessToken);
          setAuthMode(zaloRes.data.authMode);
          setCurrentUser(zaloRes.data.user);
          setLinkedChildren(zaloRes.data.linkedChildren);
          if (zaloRes.data.linkedChildren.length > 0) {
            setSelectedStudentId(zaloRes.data.linkedChildren[0].id);
          }
          setIsInitializing(false);
          return;
        }
      }

      const demoRes = await loginDemoSession(role);
      setToken(demoRes.data.accessToken);
      setAuthMode(demoRes.data.authMode);
      setCurrentUser(demoRes.data.user);
      setLinkedChildren(demoRes.data.linkedChildren);
      if (demoRes.data.linkedChildren.length > 0) {
        setSelectedStudentId((prev) => {
          const exists = demoRes.data.linkedChildren.some((c) => c.id === prev);
          return exists ? prev : demoRes.data.linkedChildren[0].id;
        });
      }
    } catch (err: unknown) {
      setAuthError(
        err instanceof Error ? err.message : 'Không thể khởi tạo phiên làm việc. Vui lòng thử lại.'
      );
    } finally {
      setIsInitializing(false);
    }
  }, []);

  useEffect(() => {
    void initializeRoleSession('PARENT');
  }, [initializeRoleSession]);

  const switchRole = useCallback(
    async (targetRole: AppRole) => {
      await initializeRoleSession(targetRole);
    },
    [initializeRoleSession]
  );

  const selectStudent = useCallback((studentId: number) => {
    setSelectedStudentId(studentId);
  }, []);

  const navigateTab = useCallback((tab: ParentScreenTab) => {
    setActiveTab(tab);
  }, []);

  const retryAuth = useCallback(async () => {
    const role = currentUser?.role || 'PARENT';
    await initializeRoleSession(role);
  }, [currentUser, initializeRoleSession]);

  const selectedStudent =
    linkedChildren.find((child) => child.id === selectedStudentId) ||
    linkedChildren[0] ||
    null;

  return (
    <AppContext.Provider
      value={{
        token,
        authMode,
        currentUser,
        linkedChildren,
        selectedStudentId,
        selectedStudent,
        activeTab,
        isInitializing,
        authError,
        switchRole,
        selectStudent,
        navigateTab,
        retryAuth,
      }}
    >
      {children}
    </AppContext.Provider>
  );
}

export function useAppContext(): AppContextValue {
  const ctx = useContext(AppContext);
  if (!ctx) {
    throw new Error('useAppContext must be used inside AppProvider');
  }
  return ctx;
}

