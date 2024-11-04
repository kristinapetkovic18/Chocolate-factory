import { createRouter, createWebHistory } from 'vue-router'
//import HomeView from '../views/HomeView.vue'
import CokoladeView from '../views/Cokolade/CokoladeView.vue'
import CokoladaIzmeni from '../views/Cokolade/CokoladaIzmeni.vue'
import CokoladeKreiraj from '../views/Cokolade/CokoladeKreiraj.vue'
import FabrikeView from '../views/Fabrike/FabrikeView.vue'
import DodajFabriku from '../views/Fabrike/FabrikeKreiraj.vue'
import Fabrika from '../views/Fabrike/Fabrika.vue'
import FabrikePretraga from '../views/Fabrike/FabrikePretraga.vue'
import Register from '../views/User/Register.vue'
import Login from '../views/User/LogIn.vue'
import KupovineView from "@/views/Kupovine/KupovineView.vue";
import UsersView from "@/views/User/UsersView.vue";
import UsersPretraga from "@/views/User/UsersPretraga.vue";
import Profile from "@/views/User/Profile.vue";


const routes = [
  // {
  //   path: '/about',
  //   name: 'about',
  //   component: () => import('../views/AboutView.vue')
  // },
  {
    path: '/',
    name: 'fabrike',
    component: FabrikeView
  },
  {
    path: '/:id',
    name: 'fabrika',
    component: Fabrika
  },
  {
    path: '/cokolade',
    name: 'cokolade',
    component: CokoladeView
  },
  {
    path:'/fabrike/kreiraj',
    name: 'fabrikeKreiraj',
    component: DodajFabriku
  },
  {
    path:'/kupovine',
    name: 'kupovine',
    component: KupovineView
  },
  {
    path:'/fabrike/pretraga',
    name: 'fabrikePretraga',
    component: FabrikePretraga
  },
  {
    path: '/cokolade/kreiraj',
    name: 'cokoladeKreiraj',
    component: CokoladeKreiraj
  },
  {
    path: '/cokolade/:id/izmeni',
    name: 'cokoladaIzmeni',
    component: CokoladaIzmeni
  },
  {
    path: '/register',
    name: 'register',
    component: Register
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/korisnici',
    name: 'korisnici',
    component: UsersView
  },
  {
    path: '/profile',
    name: 'profile',
    component: Profile
  },
  {
    path: '/korisnici/pretraga',
    name: 'UsersSearch',
    component: UsersPretraga
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

export default router
