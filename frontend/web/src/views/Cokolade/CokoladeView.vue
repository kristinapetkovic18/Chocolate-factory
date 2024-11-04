<template>
  <div class="container">
    <div class="card">
      <div class="card-header">
        <h4 v-if="RadnikLoggedIn">Sve cokolade u mojoj fabrici</h4>
        <h4 v-if="ManagerLoggedIn">Sve cokolade u mojoj fabrici
          <RouterLink   to="cokolade/kreiraj" class="btn btn-primary float-end">Dodaj novu cokoladu</RouterLink>
        </h4>
        <h4 v-if="!RadnikLoggedIn && !ManagerLoggedIn">Sve cokolade</h4>
      </div>

      <div class="card-body">
        <table class="table table-bordered">
          <thead>
            <tr>
              <th></th>
              <th>IME</th>
              <th>OPIS</th>
              <th>NA STANJU</th>
              <th>VRSTA</th>
              <th>FABRIKA ID</th>
              <th>GRAMAŽA</th>
              <th>CENA</th>
              <th>KOLIČINA</th>
              <th v-if="ManagerLoggedIn">AKCIJE</th>
            </tr>
          </thead>
          <tbody v-if="cokolade.length > 0">
            <tr v-for="(cokolada, index) in cokolade" :key="index">

              <td>
                <img :src="'../src/assets/pictures/'+ cokolada.putanja" alt="Slika fabrike" class="img-thumbnail" width="100">
              </td>
              <td>{{ cokolada.ime }}</td>
              <td>{{ cokolada.opis }}</td>
              <td>{{ cokolada.naStanju }}</td>
              <td>{{ getVrstaLabel(cokolada.vrsta) }}</td>
              <td>{{ cokolada.fabrikaId }}</td>
              <td>{{ cokolada.gramaza }}</td>
              <td>{{ cokolada.cena }}</td>
              <td>{{ cokolada.kolicina }}</td>

              <td  v-if="ManagerLoggedIn">
                <RouterLink :to="{path: '/cokolade/' + cokolada.id + '/izmeni'}" v-if="ManagerLoggedIn" class="btn btn-danger float-end">IZMENI</RouterLink>
                <button type="button"  @click = "obrisiCokoladu(cokolada.id)" v-if="ManagerLoggedIn" class="btn btn-primary"> OBRISI</button>
              </td>
              <td  v-if="RadnikLoggedIn">
                <RouterLink :to="{path: '/cokolade/' + cokolada.id + '/izmeni'}" class="btn btn-danger float-end">IZMENI KOLICINU</RouterLink>
               </td>
            </tr>
          </tbody>
          <tbody v-else>
            <tr>
              <td colspan="11">Loading...</td>
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
  name: "fabrike",
  data() {
    return {
      cokolade: [],
      ManagerLoggedIn: false,
      RadnikLoggedIn: false,
      loggedUserFabrikId: 0
    };
  },
  mounted() {

  },
  created() {
    this.checkLogin();
  },
  methods: {
    getCokolade( ) {
      if(this.RadnikLoggedIn || this.ManagerLoggedIn){
        axios.get('http://localhost:8081/WebShopAppREST/rest/cokolada/svefabrike'+this.loggedUserFabrikId)
            .then(res => {
              this.cokolade = res.data;
              console.log(res);
            });
      } else {
        axios.get('http://localhost:8081/WebShopAppREST/rest/cokolada/sve')
            .then(res => {
              this.cokolade = res.data;
              console.log(res);
            });
      }

    },
    getVrstaLabel(vrsta) {
      switch (vrsta) {
        case 0:
          return 'MLECNA';
        case 1:
          return 'DARK';
        case 2:
          return 'BELA';
        default:
          return 'Unknown';
      }
    },
    obrisiCokoladu(id){

      axios.delete(`http://localhost:8081/WebShopAppREST/rest/cokolada/delete/${id}`)
        .then(response => {
          console.log('Cokolada deleted successfully');

          this.getCokolade();
        })
    },
    checkLogin() {
      axios.get('http://localhost:8081/WebShopAppREST/rest/users/loggedUser')
          .then(response => {
            if (response.data) {
              if(response.data.role === 2){
                this.ManagerLoggedIn = true;
                this.loggedUserFabrikId = response.data.fabrikaCokoladeId
              }
              if(response.data.role === 1){
                this.RadnikLoggedIn = true;
                this.loggedUserFabrikId = response.data.fabrikaCokoladeId
              }


              this.getCokolade();
            }
          })
          .catch(error => {
            console.log(error);
          });

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
