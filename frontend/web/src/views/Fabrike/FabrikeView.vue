<template>
    <div class="container">
    
      <div class="card">
        <div class= "card-header">
          <h4>Dobro dosli
            <RouterLink
                v-if="AdminLoggedIn"
                to="fabrike/kreiraj"
                class="btn btn-primary float-end me-2">
              Dodaj Fabriku
            </RouterLink>
            <RouterLink
                to="fabrike/pretraga"
                class="btn btn-primary float-end  me-2">
              Napredna pretraga
            </RouterLink>
          </h4>
      
    </div>
      
    <div class = "card-body">

      <table class = "table table-bordered">
        <thead>
          <tr>
            <th>
              SLIKA
            </th>
            <th>
              IME
            </th>
            <th>
              LOKACIJA
            </th>
            <th>
              OD
            </th>
            <th>
              DO
            </th>
            <th>
              PROSECNA OCENA
            </th>
            <th>
              DETALJNIJE
            </th>
          </tr>
        </thead>
        <tbody v-if = "this.fabrike.length >0">
          <tr v-for="(fabrika, index) in this.fabrike" :key = "index">

            <td>
              <img :src="'../src/assets/pictures/'+ fabrika.slika" alt="Slika fabrike" class="img-thumbnail" width="100">
            </td>
            <td>{{fabrika.ime}}</td>
            <td>{{fabrika.location.address.street}}</td>
            <td>{{fabrika.radnoVreme.startTime}}</td>   
            <td>{{fabrika.radnoVreme.endTime}}</td>   
            <td>{{fabrika.prosecnaOcena}}</td> 
            <td>
              <RouterLink :to="{path: '/' + fabrika.id }" class="btn btn-danger ">
                PREGLED
              </RouterLink>
            </td>
          </tr>
        </tbody>
     <tbody  v-else >
      <tr>
        <td colspan = "7" > Loading ..</td>
      </tr>
     </tbody>
     
      </table>
    </div>
    </div>
  
  </div>
  </template>
  
  <script>
 import axios from 'axios'
  export default{
    name: "fabrike",
    data(){
      return {
        fabrike:[],
        AdminLoggedIn: false,
        usersFabrikaId: 0,
        ManagerOrEmployee: false
      }
    },
    mounted(){
        this.checkLogin();
    },
    async created() {
       await this.checkLogin();
    },
    methods:{
      getFabrike(){
          axios.get(' http://localhost:8081/WebShopAppREST/rest/fabrika/sve').then(res =>{
            this.fabrike = res.data
            if (this.ManagerOrEmployee){
              this.fabrike = res.data.filter(fabrika =>fabrika.id === this.usersFabrikaId);
            }
            console.log(res)
          });

      },
    checkLogin() {
      axios.get('http://localhost:8081/WebShopAppREST/rest/users/loggedUser')
          .then(response => {
            if (response.data.role === 3) {
              this.AdminLoggedIn = true;
            }
            if (response.data.role === 2 || response.data.role === 1 ) {
              this.ManagerOrEmployee = true;
              this.usersFabrikaId = response.data.fabrikaCokoladeId
            }
          })
          .catch(error => {
            console.log(error);
          });


      this.getFabrike();
    }
    },
    watch: {
      '$route'(to, from) {
        this.checkLogin();
        this.getFabrike();

      }
    }
  }

</script>
  <style>
  .img-thumbnail {
  max-width: 100px;
  height: auto;
}
  @media (min-width: 1024px) {
    .about {
      min-height: 100vh;
      display: flex;
      align-items: center;
    }
  }
  </style>
  