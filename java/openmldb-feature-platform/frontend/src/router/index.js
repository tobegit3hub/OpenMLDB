import { createRouter, createWebHistory } from 'vue-router'

import HomePage from '../components/HomePage.vue'
import TablesPage from '../components/TablesPage.vue'
import EntitiesPage from '../components/EntitiesPage.vue'
import FeaturesPage from '../components/FeaturesPage.vue'
import FeatureViewsPage from '../components/FeatureViewsPage.vue'
import FeatureServicesPage from '../components/FeatureServicesPage.vue'
import SqlPage from '../components/SqlPage.vue'
import TutorialPage from '../components/TutorialPage.vue'

const router = createRouter({
  history: createWebHistory("/"),
  routes: [
    { path: '/', component: HomePage },
    { path: '/tables', component: TablesPage},
    { path: '/entities', component: EntitiesPage},
    { path: '/features', component: FeaturesPage},
    { path: '/featureviews', component: FeatureViewsPage},
    { path: '/featureservices', component: FeatureServicesPage},
    { path: '/sql', component: SqlPage},
    { path: '/tutorial', component: TutorialPage},
    { path: '/404', redirect: "/" },
    { path: '/:pathMatch(.*)*', redirect: "/404" }
  ]
})

export default router
