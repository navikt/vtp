import Vue from 'vue';
import Router from 'vue-router';
import Datascenarier from './views/Datascenarier.vue';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'datascenarier',
      component: Datascenarier,
    },
    {
      path: '/soknader',
      name: 'soknader',
      component: () => import('@/views/Soknader.vue'),
    },
  ],
});
