<template>
  <div class="container mt-5">
    <div class="card">
      <div class="card-header">
        <h4>Login</h4>
      </div>
      <div class="card-body">
        <div class="mb-3">
          <label for="username">Korisničko ime*</label>
          <input type="text" v-model="login.username" class="form-control" id="username" name="username" required>
        </div>
        <div class="mb-3">
          <label for="password">Šifra*</label>
          <input type="password" v-model="login.password" class="form-control" id="password" name="password" required>
        </div>
        <div class="mb-3">
          <button type="button" @click="loginUser" class="btn btn-primary">Login</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'login',
  data() {
    return {
      login: {
        username: '',
        password: ''
      }
    };
  },
  methods: {
    validateForm() {
      const login = this.login;
      if (!login.username || !login.password) {
        alert('Popunite obavezna polja.');
        return false;
      }
      return true;
    },
    loginUser() {
      if (!this.validateForm()) {
        return;
      }
      axios.post('http://localhost:8081/WebShopAppREST/rest/users/login', this.login)
          .then(res => {
            console.log('Uspesno ste se prijavili!');
            this.$router.push({ name: 'fabrike' }); })
          .catch(error => {
            if (error.response) {
              alert('Neuspesna prijava.');
              console.log(error.response.data);
              console.log(error.response.status);
            }
          });
    }
  }
};
</script>
