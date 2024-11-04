<template>
  <div class="container">
    <div class="card">
      <div class="card-header">
        <h4>Svi korisnici
          <RouterLink to="register"
                      class="btn btn-primary float-end me-2">Dodaj Korisnika</RouterLink>
          <RouterLink to="korisnici/pretraga"
                      class="btn btn-primary float-end me-2">Napredna pretraga</RouterLink>

        </h4>
      </div>

      <div class="card-body">
        <table class="table table-bordered">
          <thead>
          <tr>
            <th @click="sortBy('firstName')">
              IME
              <span v-if="sortKey === 'firstName'">{{ sortOrder === 1 ? '🔼' : '🔽' }}</span>
            </th>
            <th @click="sortBy('lastName')">
              PREZIME
              <span v-if="sortKey === 'lastName'">{{ sortOrder === 1 ? '🔼' : '🔽' }}</span>
            </th>
            <th @click="sortBy('username')">
              KORISNIČKO IME
              <span v-if="sortKey === 'username'">{{ sortOrder === 1 ? '🔼' : '🔽' }}</span>
            </th>
            <th>POL</th>
            <th>DATUM ROĐENJA</th>
            <th>TIP KUPCA</th>
            <th @click="sortBy('bodovi')">
              BODOVI
              <span v-if="sortKey === 'bodovi'">{{ sortOrder === 1 ? '🔼' : '🔽' }}</span>
            </th>
          </tr>
          </thead>
          <tbody v-if="korisnici.length > 0">
          <tr v-for="(korisnik, index) in sortedKorisnici" :key="index">
            <td>{{ korisnik.firstName }}</td>
            <td>{{ korisnik.lastName }}</td>
            <td>{{ korisnik.username }}</td>
            <td>{{ korisnik.gender === 1 ? 'Muški' : 'Ženski' }}</td>
            <td>{{ korisnik.dateOfBirth }}</td>
            <td v-if="korisnik.role === 0">{{ korisnik.tipKupca.name }}</td>
            <td v-else></td>
            <td v-if="korisnik.role === 0">{{ korisnik.bodovi }}</td>
            <td v-else></td>
          </tr>
          </tbody>
          <tbody v-else>
          <tr>
            <td colspan="7">Loading...</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "korisnici",
  data() {
    return {
      korisnici: [],
      role: 5,
      sortKey: '',
      sortOrder: 1
    };
  },
  computed: {
    sortedKorisnici() {
      return this.korisnici.sort((a, b) => {
        let modifier = this.sortOrder;
        if (a[this.sortKey] < b[this.sortKey]) return -1 * modifier;
        if (a[this.sortKey] > b[this.sortKey]) return 1 * modifier;
        return 0;
      });
    }
  },
  mounted() {
    this.getKorisnici()

  },
  methods: {
    getKorisnici() {
      axios.get('http://localhost:8081/WebShopAppREST/rest/users/get').then(res => {
        this.korisnici = res.data;
      });
    },

    checkLogin() {
      axios.get('http://localhost:8081/WebShopAppREST/rest/users/loggedUser')
          .then(response => {
            this.role = response.data.role
          })
          .catch(error => {
            console.log(error);
          });

      //pozovi GET USER BY ID S
    },
    sortBy(key) {
      if (this.sortKey === key) {
        this.sortOrder *= -1;
      } else {
        this.sortOrder = 1;
      }
      this.sortKey = key;
    }
  }
};
</script>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
