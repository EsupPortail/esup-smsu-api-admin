import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  base: '',
  build: {
    outDir: '../src/main/webapp/'
  },
  plugins: [vue()],
  resolve: {
    alias: {
        'vue': 'vue/dist/vue.esm-bundler.js'
    },
  }
})
