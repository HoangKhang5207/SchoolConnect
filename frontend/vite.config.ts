import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import zaloMiniApp from 'zmp-vite-plugin';
import path from 'path';

export default defineConfig({
  plugins: [zaloMiniApp(), react()],
  envDir: path.resolve(__dirname, '../'),
  build: {
    assetsInlineLimit: 0,
    outDir: 'www',
    emptyOutDir: true,
  },
  server: {
    port: 3000,
    host: true,
  },
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: './src/testSetup.ts',
  },
});