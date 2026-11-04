import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

const gateway = process.env.VITE_GATEWAY_TARGET || 'http://127.0.0.1:8080';

export default defineConfig({
  base: '/app/',
  plugins: [react()],
  server: {
    host: '0.0.0.0',
    proxy: {
      '/api': gateway,
      '/bff': gateway,
      '/print': gateway,
    },
  },
});