<script setup>
import { RouterLink, useRouter } from 'vue-router';
import { ref, onMounted } from 'vue';
import axios from 'axios';

const userRole = ref(localStorage.getItem('userRole') ? parseInt(localStorage.getItem('userRole')) : -1);
const router = useRouter();

function checkLogin() {
  axios.get('http://localhost:8081/WebShopAppREST/rest/users/loggedUser')
      .then(response => {
        if (response.data) {
          userRole.value = response.data.role;
          localStorage.setItem('userRole', userRole.value);
          localStorage.setItem('id', response.data.id);
          console.log(response.data);
        }
      })
      .catch(error => {
        console.log(error);
      });
}

function logout() {
  axios.post('http://localhost:8081/WebShopAppREST/rest/users/logout')
      .then(() => {
        userRole.value = -1;
        localStorage.removeItem('userRole');  // Remove role from localStorage
        localStorage.removeItem('id');
        router.push('/login');
      })
      .catch(error => {
        console.log(error);
      });
}

onMounted(() => {
  checkLogin();  // Ensure to call this on component load to check if the user is logged in
});
</script>

<template>
  <header>
    <div class="wrapper">
      <nav class="navbar navbar-expand-lg  bg-body-tertiary">
        <div class="container-fluid">
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
              <li class="nav-item">
                <RouterLink class="nav-link" to="/">Fabrike</RouterLink>
              </li>
              <li class="nav-item">
                <RouterLink class="nav-link" to="/cokolade">Cokolade</RouterLink>
              </li>
              <li class="nav-item" v-if="userRole === -1 || userRole === 2 || userRole === 3">
                <RouterLink class="nav-link" to="/register">Registracija</RouterLink>
              </li>
              <li class="nav-item" v-if="userRole !== -1">
                <RouterLink class="nav-link" to="/profile">Moj profil</RouterLink>
              </li>
              <li class="nav-item" v-if="userRole === -1">
                <RouterLink class="nav-link" to="/login">Uloguj se</RouterLink>
              </li>
              <li class="nav-item" v-if="userRole === 3">
                <RouterLink class="nav-link" to="/korisnici">Korisnici</RouterLink>
              </li>
              <li class="nav-item" v-if="userRole === 0">
                <RouterLink class="nav-link" to="/kupovine">Moje kupovine</RouterLink>
              </li>

              <li class="nav-item" v-if=" userRole === 2">
                <RouterLink class="nav-link" to="/kupovine">Kupovine</RouterLink>
              </li>
              <li class="nav-item" v-if="userRole !== -1">
                <button class="nav-link btn btn-link" @click.prevent="logout"><b>Odjavi se</b></button>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </div>
  </header>
</template>

<style scoped>
</style>
