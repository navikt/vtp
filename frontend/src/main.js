import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store/store'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import TreeView from "vue-json-tree-view"

export const EventBus = new Vue();

Vue.config.productionTip = false

Vue.use(BootstrapVue)
Vue.use(TreeView)



new Vue({
  router,
  store,
  render: h => h(App),
  created: function() {
    this.$store.dispatch('setBackendHost'); // kontekstavhengig compute av host for backend, i.e. https vs http og localhost vs deployed
    this.$store.dispatch('setApiPath', '/rest/api');

  }
}).$mount('#app')
