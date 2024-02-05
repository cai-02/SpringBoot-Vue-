const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

// const webpack = require('webpack');
// module.exports = {
//   configureWebpack: {
//     devServer: {
//       proxy: {
//         '/api': {
//           target: 'http://47.108.66.150:9000',
//           changeOrigin: true,
//           ws: true,
//           pathRewrite: {
//             '^/api': ''
//           }
//         }
//       }
//     }
//   }
// }
