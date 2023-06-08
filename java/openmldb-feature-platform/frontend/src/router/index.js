import { createRouter, createWebHistory } from 'vue-router'

import HomePage from '../components/HomePage.vue'
import TableList from '../components/TableList.vue'
import EntitiesPage from '../components/EntitiesPage.vue'
import FeatureList from '../components/FeatureList.vue'
import FeatureViewList from '../components/FeatureViewList.vue'
import FeatureServiceList from '../components/FeatureServiceList.vue'



const router = createRouter({
  history: createWebHistory("/"),
  routes: [
    { path: '/', component: HomePage },
    { path: '/tables', component: TableList},
    { path: '/entities', component: EntitiesPage},
    { path: '/features', component: FeatureList},
    { path: '/featureviews', component: FeatureViewList},
    { path: '/featureservices', component: FeatureServiceList},
    { path: '/404', redirect: "/" },
    { path: '/:pathMatch(.*)*', redirect: "/404" }
  ]
})

export default router
