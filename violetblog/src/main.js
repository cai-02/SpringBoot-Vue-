import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
//添加全局样式
import './assets/css/global.css'
//引入iconfont
import './assets/font/iconfont.css'
import './assets/font/iconfont.js'
//时间格式
import moment from 'moment'

//导入axios跨域
import axios from 'axios'
//挂载axios
Vue.prototype.$http = axios
//设置访问根路径
//axios.defaults.baseURL = "http://47.108.66.150:9000"
axios.defaults.baseURL = "http://localhost:9000"

//时间格式
Vue.filter('dateFormat', function (dateStr,pattern = "YYYY-MM-DD HH:mm:ss") {
  return moment(dateStr).format(pattern);
})
//时间格式二
Vue.filter('dateFormat2', function (dateStr,pattern = "YYYY-MM-DD") {
  return moment(dateStr).format(pattern);
})
//时间三
Vue.filter('dateFormat3', function(value) {
  const currentTime = new Date();
  const previousTime = new Date(value);
  const timeDifference = currentTime - previousTime;
  const minutes = Math.floor(timeDifference / (60 * 1000));
  const hours = Math.floor(timeDifference / (60 * 60 * 1000));
  const days = Math.floor(timeDifference / (24 * 60 * 60 * 1000));
  if (minutes < 60) {
    return `${minutes}分钟前`;
  } else if (hours < 24) {
    return `${hours}小时前`;
  } else {
    return `${days}天前`;
  }
});
//时间四
export function formatDate(fmt, datestr) {
  var date = new Date(datestr);
  let ret;
  const opt = {
    "Y+": date.getFullYear().toString(),        // 年
    "m+": (date.getMonth() + 1).toString(),     // 月
    "d+": date.getDate().toString(),            // 日
    "H+": date.getHours().toString(),           // 时
    "M+": date.getMinutes().toString(),         // 分
    "S+": date.getSeconds().toString()          // 秒
    // 有其他格式化字符需求可以继续添加，必须转化成字符串
  };
  for (let k in opt) {
    ret = new RegExp("(" + k + ")").exec(fmt);
    if (ret) {
      fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
    };
  };
  return fmt;
}

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
