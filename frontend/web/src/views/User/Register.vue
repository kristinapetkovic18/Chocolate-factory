<template>
    <div class="container mt-5">
      <div  v-if="!isLoggedIn">
        <div class="ag-format-container">
          <div class="ag-courses_box">
            <div class="ag-courses_item">
              <a href="#" class="ag-courses-item_link">
                <div class="ag-courses-item_bg"></div>
                <div class="ag-courses-item_title">
                  Registracijom dobijas 10 bodova na poklon! Akcija traje do 10.10.2024.!
                  Sakupi do 100 i postani Bronzani clan
                </div>
              </a>
            </div>

            <div class="ag-courses_item">
              <a href="#" class="ag-courses-item_link">
                <div class="ag-courses-item_bg"></div>

                <div class="ag-courses-item_title">
                  Sa sakupljenih 100 bodova postajes Bronzani clan i ostvarujes popust od 10% !
                </div>
              </a>
            </div>

            <div class="ag-courses_item">
              <a href="#" class="ag-courses-item_link">
                <div class="ag-courses-item_bg"></div>

                <div class="ag-courses-item_title">
                  Vrednost bodova od 100 do 200 te cini Srebrnim! Popust 15%
                </div>
              </a>
            </div>

          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <h4>Kreiraj nalog:</h4>
        </div>
        <div class="card-body">

          <div class="mb-3">
            <label for="firstName">Ime*</label>
            <input type="text" v-model="model.kupac.firstName" class="form-control" id="firstName" name="firstName" required>
          </div>
          <div class="mb-3">
            <label for="lastName">Prezime*</label>
            <input type="text" v-model="model.kupac.lastName" class="form-control" id="lastName" name="lastName" required>
          </div>
          <div class="mb-3">
            <label for="gender">Pol*</label>
            <select v-model="model.kupac.gender" class="form-control" id="gender" name="gender" required>
              <option value="1">Ženski</option>
              <option value="0">Muški</option>
            </select>
          </div>
          <div class="mb-3">
            <label for="dateOfBirth">Datum rođenja*</label>
            <input type="date" v-model="model.kupac.dateOfBirth" class="form-control" id="dateOfBirth" name="dateOfBirth" required>
          </div>
          <div>
            <div class="mb-3">
              <label for="username">Korisničko ime*</label>
              <input type="text" v-model="model.kupac.username" class="form-control" id="username" name="username" required>
              <div v-if="usernameValidationMessage" class="invalid-feedback">{{ usernameValidationMessage }}</div>
            </div>
          </div>
          <div>
            <div>
              <div class="mb-3">
                <label for="password">Šifra*</label>
                <input type="password" v-model="model.kupac.password" class="form-control" id="password" name="password" required>
              </div>
              <div v-if="!isLoggedIn">
                <div class="mb-3">
                  <label for="password">Unesi ponovo sifru*</label>
                  <input type="password" v-model="repeatedpassword" class="form-control" id="password" name="password" required>
                </div>
                <div v-if="!passwordsMatch" class="text-danger">
                  Šifre se ne poklapaju.
                </div>
              </div>

            </div>
          </div>
          <div  v-if=" AdminLoggedIn">
            <div v-if="fabrikaId">
              <p>Novom menadzeru ce biti dodeljena fabrika ciji je ID : {{ fabrikaId }}</p>
            </div>
            <div v-else>
              <label for="fabrikaId">Izaberi fabriku:</label>
              <select class="form-control" id="fabrika" name="fabrikaId" v-model="model.kupac.fabrikaCokoladeId" >
                <option value="">...</option>
                <option v-for="fabrika in fabrike" :key="fabrika.id" :value="fabrika.id">
                  {{ fabrika.ime }}
                </option>
              </select>
            </div>
          </div>

          <div class="mb-3" v-if="ManagerLoggedIn || AdminLoggedIn" >
            <label for="role">Uloga*</label>
            <select v-model="model.kupac.role" class="form-control" id="role" name="role" required>
              <option value="1" v-if="ManagerLoggedIn" >Radnik</option>
              <option value="2" v-if="AdminLoggedIn" >Menadzer</option>
            </select>
          </div>
          <div class="mb-3">
            <button  v-if="ManagerLoggedIn || AdminLoggedIn" type="button" @click="saveOstali" class="btn btn-primary">REGISTRUJ</button>
            <button v-else  type="button" @click="saveKupac" class="btn btn-primary">REGISTRUJ SE</button>

          </div>
        </div>
      </div>
    </div>
</template>
<script>
import axios from 'axios';

export default {
  name: 'register',
  data() {
    return {
      fabrike: [],
      isLoggedIn: false,
      ManagerLoggedIn: false,
      AdminLoggedIn: false,
      fabrikaId: null,
      usernameValidationMessage: '',
      repeatedpassword: '',
      menadzerovaFabrikaId: 0,
      model: {
        kupac: {
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
        }
      }
    };
  },
  computed: {
    passwordsMatch() {
      return this.model.kupac.password === this.repeatedpassword;
    }
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
    this.fetchFabrike();
    this.fabrikaId = this.$route.query.fabrikaId;
    console.log('Received fabrikaId:', this.fabrikaId);
  },
  methods: {
    async fetchFabrike() {
      try {
        const response = await axios.get('http://localhost:8081/WebShopAppREST/rest/fabrika/sve');
        this.fabrike = response.data;
        console.log(response.data)
      } catch (error) {
        console.error('Error fetching fabrike:', error);
      }
    },
    checkLogin() {
      axios.get('http://localhost:8081/WebShopAppREST/rest/users/loggedUser')
          .then(response => {
            if (response.data) {
              this.isLoggedIn = true;
              this.menadzerovafabrikaId = response.data.fabrikaCokoladeId;
              console.log('ulogovanog menadzera fabrika je ID:')
              console.log(this.menadzerovafabrikaId)

              if(response.data.role === 2){
                this.ManagerLoggedIn = true;
              }
              if(response.data.role === 3){
                this.AdminLoggedIn = true;
              }
            }
          })
          .catch(error => {
            console.log(error);
          });

      //pozovi GET USER BY ID S
    },
    validateForm() {
      const kupac = this.model.kupac;
      if (!kupac.firstName || !kupac.lastName || !kupac.gender || !kupac.dateOfBirth ||
          !kupac.username || !kupac.password ) {
        alert('Popunite obavezna polja.');
        return false;
      }
      return true;
    },
    prepareDataForSave() {
      // Convert gender and role to integers
      this.model.kupac.gender = parseInt(this.model.kupac.gender, 10);
      this.model.kupac.role = parseInt(this.model.kupac.role, 10);
    },
    saveKupac() {
     /* if (!this.validateForm()) {
        return;
      }*/
      this.prepareDataForSave();
      if(this.ManagerLoggedIn){
        //manager radnike
        this.model.kupac.role = 1
      }
      if(this.AdminLoggedIn){
        //admin managere registruje
        this.model.kupac.role = 2
      }else
      {
        this.model.kupac.role = 0
      }


      axios.post('http://localhost:8081/WebShopAppREST/rest/users/registration', this.model.kupac)
          .then(res => {
            alert('Uspesno ste kreirali nalog!');
            this.model.kupac = {
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
              tipKupca: '',
              bodovi: 0
            };
          })
          .catch(error => {
            if (error.response) {
              console.log(error.response.data);
              console.log(error.response.status);
            }
          });
    },
    saveOstali() {
      this.prepareDataForSave();
      if(this.ManagerLoggedIn){
        //manager radnike
        this.model.kupac.fabrikaCokoladeId = this.menadzerovafabrikaId;
        this.model.kupac.role = 1
      }
      if(this.AdminLoggedIn){
        //admin managere registruje
        this.model.kupac.role = 2
      }

      if (this.fabrikaId) {
        this.model.kupac.fabrikaCokoladeId = this.fabrikaId;
      }
//Kupac, Radnik, Menadžer, Administrator
      axios.post('http://localhost:8081/WebShopAppREST/rest/users/novimenadzeriliradnik', this.model.kupac)
          .then(res => {
            alert('Uspesno ste kreirali nalog!');
            this.model.kupac = {
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
              fabrikaCokoladeId: ''
            };
            this.fabrikaId = null;
          })
          .catch(error => {
            if (error.response) {
              console.log(error.response.data);
              console.log(error.response.status);
            }
          });
    },
    update() {
      if (!this.validateForm()) {
        return;
      }
      axios.post('http://localhost:8081/WebShopAppREST/rest/users/update', this.model.kupac)
          .then(res => {
            alert('Uspesno ste kreirali nalog!');
            this.model.kupac = {
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
              fabrikaCokoladeId: ''
            };
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


body {
  background-color: #a5bce5;
}
.ag-courses_box {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: start;
  -ms-flex-align: start;
  align-items: flex-start;
  -ms-flex-wrap: wrap;
  flex-wrap: wrap;

  padding: 50px 0;
}
.ag-courses_item {
  -ms-flex-preferred-size: calc(33.33333% - 30px);
  flex-basis: calc(33.33333% - 30px);

  margin: 0 15px 30px;

  overflow: hidden;

  border-radius: 28px;
}
.ag-courses-item_link {
  display: block;
  padding: 30px 20px;
  background-color: #364a60;

  overflow: hidden;

  position: relative;
}
.ag-courses-item_link:hover,
.ag-courses-item_link:hover .ag-courses-item_date {
  text-decoration: none;
  color: #FFF;
}
.ag-courses-item_link:hover .ag-courses-item_bg {
  -webkit-transform: scale(10);
  -ms-transform: scale(10);
  transform: scale(10);
}
.ag-courses-item_title {
  min-height: 87px;
  margin: 0 0 25px;

  overflow: hidden;

  font-weight: bold;
  font-size: 30px;
  color: #FFF;

  z-index: 2;
  position: relative;
}
.ag-courses-item_date-box {
  font-size: 18px;
  color: #FFF;

  z-index: 2;
  position: relative;
}
.ag-courses-item_date {
  font-weight: bold;
  color: #f9b234;

  -webkit-transition: color .5s ease;
  -o-transition: color .5s ease;
  transition: color .5s ease
}
.ag-courses-item_bg {
  height: 128px;
  width: 128px;
  background-color: #d75228;

  z-index: 1;
  position: absolute;
  top: -75px;
  right: -75px;

  border-radius: 50%;

  -webkit-transition: all .5s ease;
  -o-transition: all .5s ease;
  transition: all .5s ease;
}
.ag-courses_item:nth-child(2n) .ag-courses-item_bg {
  background-color: #626161;
}
.ag-courses_item:nth-child(3n) .ag-courses-item_bg {
  background-color: #d9bc00;
}


@media only screen and (max-width: 979px) {
  .ag-courses_item {
    -ms-flex-preferred-size: calc(50% - 30px);
    flex-basis: calc(50% - 30px);
  }
  .ag-courses-item_title {
    font-size: 24px;
  }
}

@media only screen and (max-width: 767px) {
  .ag-format-container {
    width: 96%;
  }

}
@media only screen and (max-width: 639px) {
  .ag-courses_item {
    -ms-flex-preferred-size: 100%;
    flex-basis: 100%;
  }
  .ag-courses-item_title {
    min-height: 72px;
    line-height: 1;

    font-size: 24px;
  }
  .ag-courses-item_link {
    padding: 22px 40px;
  }
  .ag-courses-item_date-box {
    font-size: 16px;
  }
}
</style>