<template>
  <div class="container mt-5">
    <div class="card">
      <div class="card-header">
        <h4>Dodaj novu cokoladu</h4>
      </div>
      <div class="card-body">


        <div class="mb-3">
          <label for="ime">Naziv čokolade*</label>
          <input type="text" v-model="model.cokolada.ime" class="form-control" id="ime" name="ime" required>
        </div>
        <div class="mb-3">
          <label for="opis">Opis*</label>
          <input type="text" v-model="model.cokolada.opis" class="form-control" id="opis" name="opis" required>
        </div>
        <div class="mb-3">
          <label for="vrsta">Vrsta cokolade*</label>
          <select v-model="model.cokolada.vrsta" class="form-control" id="vrsta" name="vrsta">
            <option value="0">MLECNA</option>
            <option value="1">DARK</option>
            <option value="2">BELA</option>
          </select>
        </div>

        <div class="mb-3">
          <label for="fabrikaId">Fabrika:</label>
          <input type="text" v-model="selectedFabrikaId" step="0.01" class="form-control" id="fabrikaId"
                 name="fabrikaId" disabled>
        </div>
        <div class="mb-3">
          <label for="gramaza">Gramaža*</label>
          <input type="number" v-model="model.cokolada.gramaza" step="0.01" class="form-control" id="gramaza"
                 name="gramaza" required>
        </div>
        <div class="mb-3">
          <label for="cena">Cena*</label>
          <input type="number" v-model="model.cokolada.cena" step="0.01" class="form-control" id="cena" name="cena" required>
        </div>
        <div class="mb-3">
          <label for="kolicina">Količina*</label>
          <input type="number" v-model="model.cokolada.kolicina" class="form-control" id="kolicina" name="kolicina" required>
        </div>
        <div class="mb-3">
          <label for="putanja">Dodaj sliku</label>
          <input type="file" class="form-control" id="putanja" name="putanja" @change="handleFileUpload">
        </div>


        <div class="mb-3">
          <button type="button" @click="saveCokolada" class="btn btn-primary"> DODAJ</button>
        </div>
      </div>
    </div>
  </div>


</template>
<script>
import axios from 'axios';

export default {
  name: 'cokoladeKreiraj',
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
          fabrikaId: '1',
          gramaza: 0.0,
          cena: 0.0,
          kolicina: 0,
          putanja: ''
        }
      },
      fabrike: [], // Array to hold the fetched data
      selectedFabrikaId: '' ,// To bind with v-model,
      role: 5
    };
  },
  created() {
    this.checkLogin();
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
  validateForm() {
    // Check if all required fields are filled out
    if (!this.model.cokolada.ime || !this.model.cokolada.opis || !this.model.cokolada.vrsta ||
        !this.model.cokolada.gramaza || !this.model.cokolada.cena ||
        !this.model.cokolada.kolicina || !this.model.cokolada.putanja) {
      alert('Popunite obavezna polja.');
      return false;
    }

    if ( this.model.cokolada.gramaza<0 ||  this.model.cokolada.cena < 0 ||
        this.model.cokolada.kolicina <0 ) {
      alert('Vrednosti moraju biti vece od 0.');
      return false;
    }
    return true;
  },
    saveCokolada() {
      this.model.cokolada.fabrikaId = this.selectedFabrikaId;
      console.log(this.model.cokolada.fabrikaId)
    //  if (!this.validateForm()) {return;}
      axios.post('http://localhost:8081/WebShopAppREST/rest/cokolada/add', this.model.cokolada).then(res => {

        console.log(res)
        alert('Nova cokolada je kreirana!')
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
          putanja: ''
        }
      }).catch(function (error) {
            if (error.response) {
              console.log(error.response.data)
              console.log(error.response.status)
            }
          }
      )
    },
    checkLogin() {
      axios.get('http://localhost:8081/WebShopAppREST/rest/users/loggedUser')
          .then(response => {
            if (response.data) {
              this.role = response.data.role;
              this.selectedFabrikaId = response.data.fabrikaCokoladeId;
            }
          })
          .catch(error => {
            console.log(error);
          });

    }

  }
};
</script>