<template>
  <div class="container mt-5">
    <div class="card">
      <div class="card-header">
        <h4 v-if="isLoggedIn">Moj profil</h4>
      </div>
      <div class="card-body">
        <div class="mb-3">
          <label for="firstName">Ime*</label>
          <input type="text" v-model="model.user.firstName" class="form-control" id="firstName" name="firstName" required>
        </div>
        <div class="mb-3">
          <label for="lastName">Prezime*</label>
          <input type="text" v-model="model.user.lastName" class="form-control" id="lastName" name="lastName" required>
        </div>
        <div class="mb-3">
          <label for="gender">Pol*</label>
          <select v-model="model.user.gender" class="form-control" id="gender" name="gender" required>
            <option value="1">Ženski</option>
            <option value="0">Muški</option>
          </select>
        </div>
        <div class="mb-3">
          <label for="dateOfBirth">Datum rođenja*</label>
          <input type="date" v-model="model.user.dateOfBirth" class="form-control" id="dateOfBirth" name="dateOfBirth" required>
        </div>
        <div>
          <div class="mb-3">
            <label for="username">Korisničko ime*</label>
            <input type="text" v-model="model.user.username" class="form-control" id="username" name="username" required>
            <div v-if="usernameValidationMessage" class="invalid-feedback">{{ usernameValidationMessage }}</div>
          </div>
        </div>
        <div>
          <div>
            <div class="mb-3">
              <label for="password">Šifra*</label>
              <input type="password" v-model="model.user.password" class="form-control" id="password" name="password" required>
            </div>
          </div>
        </div>
        <div v-if="model.user.role === 1 || model.user.role === 2">
          <div>
            <div class="mb-3">
              <label for="fId">Zaposlen u fabrici ciji je ID:</label>
              <input :disabled="!AdminLoggedIn" type="text" v-model="model.user.fabrikaCokoladeId" class="form-control" id="fId" name="fId" required>
            </div>
          </div>
        </div>
        <div class="mb-3" >
          <label for="role">Uloga*</label>
          <select v-model="model.user.role" class="form-control" id="role" name="role" disabled>
            <option value="0">Kupac</option>
            <option value="1">Radnik</option>
            <option value="2">Menadzer</option>
          </select>

          <div v-if="model.user.role ===0">

            <div class="mb-3">
              <label for="n">Tip kupca ime*</label>
              <input type="text"
                     v-model="model.user.tipKupca.name"
                     :class="{'gold-text': model.user.tipKupca.name === 'GOLD' ||  model.user.tipKupca.name === 'NONE',
                     'silver-text': model.user.tipKupca.name === 'SILVER',
                     'bronze-text': model.user.tipKupca.name === 'BRONZE'}"
                     class="form-control"
                     id="n"
                     name="n"
                     disabled
                     ></div>
            <div v-if="model.user.tipKupca.name === 'GOLD' || model.user.tipKupca.name === 'NONE'" class="medal-icon">
              <i class="fas fa-check-circle"></i>
            </div>
            <div class="mb-3">
              <label for="d">Tip kupca popust*</label>
              <input type="text" v-model="model.user.tipKupca.discount" class="form-control" id="d" name="d" disabled>
            </div>
            <div class="mb-3">
              <label for="b">Trenutno bodova</label>
              <input type="text" v-model="model.user.bodovi" class="form-control" id="b" name="b" disabled>
            </div>
            <div class="mb-3">
              <label for="p">Bodova fali*</label>
              <input type="text" v-model="model.user.tipKupca.pointsRequired" class="form-control" id="p" name="" disabled>
            </div>
          </div>

        </div>
        <div class="mb-3">
          <button   type="button" @click="update" class="btn btn-primary">AZURIRAJ PODATKE</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios';

export default {
  name: 'profile',
  data() {
    return {
      fabrike: [],
      isLoggedIn: false,
      BiloKoSemKupcaLoggedIn: false,
      isLoggedInManagerOrAdmin: false,
      AdminLoggedIn: false,
      usernameValidationMessage: '',
      repeatedpassword: '',
      model: {
        user: {
          id: '',
          deleted: false,
          firstName: '',
          lastName: '',
          gender: '',
          dateOfBirth: '',
          username: '',
          password: '',
          role: '',
          korpaId: '',
          fabrikaCokoladeId: '',
          tipKupca: {
            name:'',
            pointsRequired:'',
            discount:''
          },
          bodovi: 0
        }
      }
    };
  },
  watch: {
    //AKO NIKO NIJE ULOGOVAN isLoggedIn
    //disable neka polja
    /*'model.kupac.username': function (newUsername) {
      if (newUsername) {
        LoginService.isUsernameTaken(newUsername)
            .then(response => {
              if (response) {
                this.usernameValidationMessage = 'Username is already taken';
              } else {
                this.usernameValidationMessage = '';
              }
            })
            .catch(error => {
              console.error('Error checking username:', error);
              this.usernameValidationMessage = 'Error checking username';
            });
      } else {
        this.usernameValidationMessage = '';
      }
    }*/
  },
  created() {
    this.checkLogin();
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
    checkLogin() {
      axios.get('http://localhost:8081/WebShopAppREST/rest/users/loggedUser')
           .then(response => {
             if (response.data) {
               this.isLoggedIn = true;
               this.model.user = response.data;
               if(response.data.role === 3){
                 this.AdminLoggedIn = true;}
             }
           })
           .catch(error => {
             console.log(error);
           });

      //pozovi GET USER BY ID S
    },
    validateForm() {
      const user = this.model.user;
      if (!user.firstName || !user.lastName || !user.gender || !user.dateOfBirth ||
          !user.username || !user.password ) {
        alert('Popunite obavezna polja.');
        return false;
      }
      return true;
    },
    prepareDataForSave() {
      // Convert gender and role to integers
      this.model.user.gender = parseInt(this.model.user.gender, 10);
      this.model.user.role = parseInt(this.model.user.role, 10);
    },
    update() {
//Kupac, Radnik, Menadžer, Administrator
      axios.post('http://localhost:8081/WebShopAppREST/rest/users/update/' + this.model.user.id, this.model.user)
          .then(res => {
            alert('Uspesno ste azurirali nalog!');
            this.checkLogin()

          })
          .catch(error => {
            if (error.response) {
              console.log(error.response.data);
              console.log(error.response.status);
            }
          });
    }
  }
};
</script>
<style>
.ag-format-container {
  width: 1142px;
  margin: 0 auto;
}
.gold-text {
  color: gold;
  font-size: 1.5em; /* Adjust the size as needed */
  font-weight: bold;
  font-style: italic; /* Add italic style */
}

.silver-text {
  color: silver;
  font-size: 1.5em; /* Adjust the size as needed */
  font-weight: bold;
  font-style: italic; /* Add italic style */
}

.bronze-text {
  color: #cd7f32;
  font-size: 1.5em; /* Adjust the size as needed */
  font-weight: bold;
  font-style: italic; /* Add italic style */
}
.medal-icon {
  font-size: 2em; /* Adjust the size as needed */
  color: gold; /* Adjust the color as needed */
  text-align: center; /* Center the icon within its container */
}

body {
  background-color: #a5bce5;
}

</style>