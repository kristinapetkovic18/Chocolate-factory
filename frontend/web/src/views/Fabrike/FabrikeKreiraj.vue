<template>
  <div class="container mt-5">
    <div class="card">
      <div class="card-header">
        <h4>Dodaj novu fabriku</h4>
      </div>
      <div class="card-body">
        <div class="mb-3">
          <label for="ime">Naziv fabrike*</label>
          <input type="text" v-model="novaFabrika.ime" class="form-control" id="ime" name="ime" required>
        </div>
        <div class="mb-3">
          <label for="lokacija">Lokacija*</label>
          <input type="text" v-model="novaFabrika.lokacija" class="form-control" id="lokacija" name="lokacija" required>
        </div>
        <div class="mb-3">
          <label for="street">Ulica*</label>
          <input type="text" v-model="novaFabrika.street" class="form-control" id="street" name="street" required>
        </div>
        <div class="mb-3">
          <label for="city">Grad*</label>
          <input type="text" v-model="novaFabrika.city" class="form-control" id="city" name="city" required>
        </div>
        <div class="mb-3">
          <label for="postalNumber">Postanski broj*</label>
          <input type="text" v-model="novaFabrika.postalNumber" class="form-control" id="postalNumber" name="postalNumber" required>
        </div>
        <div class="mb-3">
          <label for="startTime">Početak radnog vremena*</label>
          <input type="time" v-model="novaFabrika.startTime" class="form-control" id="startTime" name="startTime" required>
        </div>
        <div class="mb-3">
          <label for="endTime">Kraj radnog vremena*</label>
          <input type="time" v-model="novaFabrika.endTime" class="form-control" id="endTime" name="endTime" required>
        </div>
        <div class="mb-3">
          <label for="putanja">Dodaj sliku</label>
          <input type="file" class="form-control" id="putanja" name="putanja" @change="handleFileUpload">
        </div>
        <div class="mb-3">

          <div v-if="menadzeri.length === 0">
            <label for="menadzerId">Nema dostupnih menadzera</label>
          </div>
          <div v-if="menadzeri.length !== 0">

            <label for="fabrikaId">Izaberi menadzera:</label>
            <select class="form-control" id="fabrika" name="fabrikaId" v-model="selectedMenadzerId" required>
              <option value="">...</option>
              <option v-for="menadzer in menadzeri" :key="menadzer.id" :value="menadzer.id">
                {{ menadzer.firstName }} {{ menadzer.lastName }}
              </option>

            </select>
          </div>

        </div>
        <div class="mb-3">
          <button  v-if = "menadzeri.length===0" type="button" @click="saveFabrika" class="btn btn-secondary">Dodaj fabriku i kreiraj menadzera</button>
          <button v-else type="button" @click="saveFabrika" class="btn btn-secondary"> Dodaj fabriku</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios';

export default {
  name: 'DodajFabriku',
  created(){
    this.fetchDostupniMenadzeri()
  },
  data() {
    return {
      menadzeri: [],
      menadzer: 0,
      selectedMenadzerId: 0,
      AdminLoggedIn: true,
      role: 5,
      fabrikaId: 0,
      novaFabrika: {
        ime: '',
        lokacija: '',
        street:'',
        city: '',
        postalNumber:'',
        startTime: '',
        endTime: '',
        putanja: ''
      }
    };
  },
  methods: {
    checkLogin() {
      axios.get('http://localhost:8081/WebShopAppREST/rest/users/loggedUser')
          .then(response => {
            if (response.data) {
              this.role = response.data.role;
              if(this.role === 3) {
                this.AdminLoggedIn = true;
              }
            }
          })
          .catch(error => {
            console.log(error);
          });

    },
    async fetchDostupniMenadzeri() {
      try {
        const response = await axios.get('http://localhost:8081/WebShopAppREST/rest/users/available');
        this.menadzeri = response.data;
      } catch (error) {
        console.error('Error fetching fabrike:', error);
      }

    },
    async saveFabrika() {
      console.log(this.novaFabrika)
      if (!this.validateForm()) {
        return;
      }

      try {
        // First, save the new factory
        const response = await axios.post('http://localhost:8081/WebShopAppREST/rest/fabrika/add', this.novaFabrika);
        this.fabrikaId = response.data.id; // Get the created factory's ID

        if (this.menadzeri.length === 0) {
          // No managers available, redirect to register page with the factory ID
          this.$router.push({
            name: 'register',
            query: { fabrikaId: this.fabrikaId } // Pass the factory ID as a query parameter
          });
        } else {
          console.log(this.selectedMenadzerId)
          await axios.post(`http://localhost:8081/WebShopAppREST/rest/users/menadzer/${this.selectedMenadzerId}/fabrika/${this.fabrikaId}`);
          alert('Nova fabrika je kreirana!');
          this.resetFabrikaForm();
        }
      } catch (error) {
        console.error('Error saving fabrika:', error);
      }



    },
    resetFabrikaForm() {
      this.novaFabrika = {
        firstName: '',
        lokacija: '',
        startTime: '',
        endTime: '',
        putanja: ''
      };
    },
    handleFileUpload(event) {
      const file = event.target.files[0];
      this.novaFabrika.putanja = file.name;
    },
    validateForm() {
      // Check if all required fields are filled out
      if (!this.novaFabrika.ime || !this.novaFabrika.lokacija || !this.novaFabrika.startTime || !this.novaFabrika.endTime) {
        alert('Popunite obavezna polja.');
        return false;
      }
      return true;
    }
  }
};
</script>
