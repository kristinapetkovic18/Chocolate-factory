<template>
  <div class="container mt-5">
    <div class="card">
      <div class="card-header">
        <h4>Izmeni cokoladu</h4>
      </div>
      <div class="card-body">

        <div class="mb-3">
          <label for="ime">Ime</label>
          <input :disabled="isWorkerLoggedIn" type="text" v-model="model.cokolada.ime" class="form-control" id="ime" name="ime">
        </div>
        <div class="mb-3">
          <label for="opis">Opis</label>
          <input :disabled="isWorkerLoggedIn" type="text" v-model="model.cokolada.opis" class="form-control" id="opis" name="opis">
        </div>
        <div class="mb-3">
          <label for="naStanju">Na Stanju</label>
          <input :disabled="isWorkerLoggedIn" type="checkbox" v-model="model.cokolada.naStanju" class="form-check-input" id="naStanju"
                 name="naStanju">
        </div>
        <div class="mb-3">
          <label for="vrsta">Vrsta</label>
          <select :disabled="isWorkerLoggedIn" v-model="model.cokolada.vrsta" class="form-control" id="vrsta" name="vrsta">
            <option value="0">MLECNA</option>
            <option value="1">DARK</option>
            <option value="2">BELA</option>
          </select></div>

        <div class="mb-3">
          <label for="fabrikaId">Fabrika</label>
          <select disabled class="form-control" id="fabrika" name="fabrikaId" v-model="model.cokolada.fabrikaId">
            <option value="">Izaberi</option>
            <option v-for="fabrika in fabrike" :key="fabrika.id" :value="fabrika.id">
              {{ fabrika.ime }}
            </option>
          </select>
        </div>
        <div class="mb-3">
          <label for="gramaza">Gramaža</label>
          <input :disabled="isWorkerLoggedIn" type="number" v-model="model.cokolada.gramaza" step="0.01" class="form-control" id="gramaza"
                 name="gramaza">
        </div>
        <div class="mb-3">
          <label for="cena">Cena</label>
          <input :disabled="isWorkerLoggedIn" type="number" v-model="model.cokolada.cena" step="0.01" class="form-control" id="cena" name="cena">
        </div>
        <div class="mb-3">
          <label for="kolicina">Količina</label>
          <input type="number" v-model="model.cokolada.kolicina" class="form-control" id="kolicina" name="kolicina">
        </div>
        <div class="mb-3">
          <label for="putanja">Slika</label>
          <input :disabled="isWorkerLoggedIn" type="file" class="form-control" id="putanja" name="putanja" @change="handleFileUpload">
        </div>


        <div class="mb-3">
          <button type="button" @click="saveCokolada" class="btn btn-primary"> AZURIRAJ</button>
        </div>
      </div>
    </div>
  </div>


</template>
<script>
import axios from 'axios';

export default {
  name: 'cokoladaIzmeni',
  data() {
    return {
      model: {
        cokolada: {
          id: '0',
          deleted: false,
          ime: '',
          opis: '',
          naStanju: false,
          vrsta: 0,
          fabrikaId: '',
          gramaza: 0.0,
          cena: 0.0,
          kolicina: 0,
          putanja: undefined
        }
      },
      fabrike: [],
      selectedFabrikaId: '',
      cokoladaId: '',
      isWorkerLoggedIn: false
    };
  },
  created() {
    console.log(this.$route.params.id);
    this.cokoladaId = this.$route.params.id;
    this.getCokolada();
    this.fetchFabrike();

  },
  methods: {
    async fetchFabrike() {
      try {
        const response = await axios.get('http://localhost:8081/WebShopAppREST/rest/fabrika/sve');
        this.fabrike = response.data;
      } catch (error) {
        console.error('Error fetching fabrike:', error);
      }
    },
    handleFileUpload(event) {
      const file = event.target.files[0];
      this.model.cokolada.putanja = file.name;
    },
    getCokolada() {

      axios.get('http://localhost:8081/WebShopAppREST/rest/cokolada/get/' + this.cokoladaId).then(
          res => {
            console.log(res);
            this.model.cokolada.id = res.data.id;
            this.model.cokolada.putanja = res.data.putanja;

            this.model.cokolada.gramaza = res.data.gramaza;
            this.model.cokolada.cena = res.data.cena;
            this.model.cokolada.kolicina = res.data.kolicina;

            this.model.cokolada.vrsta = res.data.vrsta;
            this.model.cokolada.fabrikaId = res.data.fabrikaId;
            this.model.cokolada.opis = res.data.opis;
            this.model.cokolada.naStanju = res.data.naStanju;
            this.model.cokolada.ime = res.data.ime;
            this.model.cokolada.deleted = res.data.deleted;

          }
      );
    },


    saveCokolada() {
      const productId = this.model.cokolada.id;
      if (!this.validateForm()) {
        return;
      }
      axios.post(`http://localhost:8081/WebShopAppREST/rest/cokolada/update/${productId}`, this.model.cokolada)
          .then(res => {
            console.log(res);

            alert('Azurirani su podaci!')
            this.cokolada = {
              id: '0',
              deleted: false,
              ime: '',
              opis: '',
              naStanju: false,
              vrsta: 0,
              fabrikaId: '',
              gramaza: 0.0,
              cena: 0.0,
              kolicina: 0,
              putanja: undefined
            };
          })
          .catch(function (error) {
            if (error.response) {
              console.log(error.response.data);
              console.log(error.response.status);
            }
          });
    },
    validateForm() {
      if ( this.model.cokolada.gramaza<0 ||  this.model.cokolada.cena < 0 ||
          this.model.cokolada.kolicina <0 ) {
        alert('Vrednosti moraju biti vece od 0.');
        return false;
      }
      return true;
    },
    checkLogin() {
      axios.get('http://localhost:8081/WebShopAppREST/rest/users/loggedUser')
          .then(response => {
            if (response.data) {
              if(response.data.role === 1){
                this.isWorkerLoggedIn = true;
              }
            }
          })
          .catch(error => {
            console.log(error);
          });

    }
  }
};
</script>