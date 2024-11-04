<template>
  <div class="container mt-5">
    <div class="card">
      <div class="card-header">
        <h4>Fabrika</h4>
        <img :src="'../src/assets/pictures/'+ model.fabrika.slika" alt="Slika fabrike" class="img-thumbnail" width="100">
      </div>
      <div class="card-body">
        <div class="mb-3">
          <label for="ime">Ime</label>
          <input type="text" disabled v-model="model.fabrika.ime" class="form-control" id="ime" name="ime">
        </div>
        <div class="mb-3">
          <label for="lokacija">Lokacija</label>
          <input type="text" disabled v-model="model.fabrika.location.address.street" class="form-control" id="lokacija" name="lokacija">
        </div>
        <div class="mb-3">
          <label for="lokacija">Radno vreme</label>
          <div>
            <label for="lokacija1">Od:</label>
            <input type="text" disabled v-model="model.fabrika.radnoVreme.startTime" class="form-control" id="lokacija" name="lokacija">
          </div>
          <div>
            <label for="lokacija2">Do:</label>
            <input type="text" disabled v-model="model.fabrika.radnoVreme.endTime" class="form-control" id="lokacija" name="lokacija">
          </div>
          </div>
        <div class="mb-3">
          <label for="status">Otvoreno</label>
          <input type="checkbox" v-model="model.fabrika.status" class="form-check-input" id="status"
                 name="status" disabled>
        </div>
        <div class="mb-3">
          <label for="prosecnaOcena">Prosecna ocena</label>
          <div id="rating-stars">
          <span v-for="(filled, index) in starRating" :key="index">
            <i :class="filled ? 'fa fa-star' : 'fa fa-star-o'"></i>
          </span>
          </div> </div>

      </div>
    </div>

  </div>

  <div class="container">
    <div class="card">
      <div class="card-header">
        <h4>Cokolade: </h4>
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
          </tr>
          </thead>
          <tbody v-if="this.model.fabrika.cokolade.length > 0">
          <tr v-for="(cokolada, index) in this.model.fabrika.cokolade" :key="index">

            <td>
              <img :src="'../src/assets/pictures/'+ cokolada.putanja" alt="Slika cokolade" class="img-thumbnail" width="100">
            </td>
            <td>{{ cokolada.ime }}</td>
            <td>{{ cokolada.opis }}</td>
            <td>{{ cokolada.naStanju }}</td>
            <td>{{ cokolada.vrsta }}</td>
            <td>{{ cokolada.fabrikaId }}</td>
            <td>{{ cokolada.gramaza }}</td>
            <td>{{ cokolada.cena }}</td>
            <td>{{ cokolada.kolicina }}</td>
          </tr>
          </tbody>
          <tbody v-else>
          <tr>
            <td colspan="10">Nema cokolada u fabrici</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <div class="container">
    <!-- User Comments Section -->
    <div class="container mt-3">
      <div class="card">
        <div class="card-header">
          <h4 v-if="this.loggedManager || this.loggedAdmin">Svi komentari</h4>
          <h4 v-else>Komentari</h4>
        </div>
        <div class="card-body">
          <div v-if="model.comments.length > 0">
            <div v-for="(komentar, index) in model.comments" :key="index" class="mb-3">
              <p><strong>{{ komentar.kupacUsername }}</strong> </p>
              <div id="rating-stars">
            <span v-for="(filled, i) in commentStarRating(komentar.ocena)" :key="i">
              <i :class="{'fa fa-star': filled, 'fa fa-star-o': !filled}"></i>
            </span>
            </div>
              <p>{{ komentar.tekst }}</p>
             <div v-if="this.loggedManager">
               <p><strong>Status: {{ komentar.status }}</strong></p>
             </div>
              <div v-if="loggedManager && komentar.status === 'Obrada'" class="mt-2">
                <button class="btn btn-success me-2" @click="acceptComment(komentar.id)">Prihvati</button>
                <button class="btn btn-danger" @click="denyComment(komentar.id)">Odbij</button>
              </div>
            </div>
          </div>
          <div v-else>
            <p>Nema komentara za ovu fabriku.</p>
          </div>
          <hr>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios';

export default {
  name: 'fabrika',
  data() {
    return {
      loggedUserId: 0,
      loggedAdmin: false,
      loggedOther: false,
      role: 0,
      loggedManager: false,
      model: {
        fabrika: {
          id: '0',
          ime: '',
          lokacija: '',
          prosecnaOcena: 0.0,
          slika: undefined,
          radnoVreme: {
            startTime: '',
            endTime: ''
          },
          location: {
            address: {
              street: '',
              city: '',
              postalNumber: ''
            },
            latitude: '',
            longitude: ''
          },
          status: false,
          cokolade: []
        },
        comments: [],
        newComment: {
          tekst: '',
          ocena: 0
        }
      }
    };
  },
  async created() {
    console.log(this.$route.params.id);
    this.fabrikaId = this.$route.params.id;
    await this.checkLogin();
    this.getFabrika();
  },
  methods: {
    getFabrika() {
      axios.get('http://localhost:8081/WebShopAppREST/rest/fabrika/get/' + this.fabrikaId).then(
          res => {
            console.log(res);
            this.model.fabrika.id = res.data.id;
            this.model.fabrika.ime = res.data.ime;
            this.model.fabrika.lokacija = res.data.lokacija;
            this.model.fabrika.prosecnaOcena = res.data.prosecnaOcena;
            this.model.fabrika.slika = res.data.slika;
            this.model.fabrika.radnoVreme.startTime = res.data.radnoVreme.startTime;
            this.model.fabrika.radnoVreme.endTime = res.data.radnoVreme.endTime;
            this.model.fabrika.status = res.data.status;
            this.model.fabrika.location.address.street = res.data.location.address.street;
            this.model.fabrika.cokolade = res.data.cokolade;
          }
      );

      this.getComments();
    },

    getComments() {
      if (!this.loggedAdmin && !this.loggedManager) {
        axios.get('http://localhost:8081/WebShopAppREST/rest/komentari/fabrika/odobreni/' + this.fabrikaId).then(
            res => {
              this.model.comments = res.data;
              console.log(res);
            }
        );
      }
      if (this.loggedAdmin || this.loggedManager) {
        axios.get('http://localhost:8081/WebShopAppREST/rest/komentari/fabrika/' + this.fabrikaId).then(
            res => {
              this.model.comments = res.data;
            }
        );
      }
    },

    acceptComment(commentId) {
      axios.put('http://localhost:8081/WebShopAppREST/rest/komentari/odobri/' + commentId).then(
          () => {
            this.getComments();
          }
      ).catch(error => {
        console.error('Error accepting comment:', error);
      });
    },

    denyComment(commentId) {
      axios.put('http://localhost:8081/WebShopAppREST/rest/komentari/odbij/' + commentId).then(
          () => {
            this.getComments(); // Refresh comments list after denying
          }
      ).catch(error => {
        console.error('Error denying comment:', error);
      });
    },

    // Modify checkLogin to return a promise
    checkLogin() {
      return axios.get('http://localhost:8081/WebShopAppREST/rest/users/loggedUser')
          .then(response => {
            if (response.data) {
              this.role = response.data.role;
              this.loggedManager = this.role === 2;
              this.loggedAdmin = this.role === 3;
              this.loggedOther = this.role === 0 || this.role === 1;

              console.log('Role:', this.role);
              console.log('Manager:', this.loggedManager);
              console.log('Admin:', this.loggedAdmin);
              console.log('Other:', this.loggedOther);
            }
          })
          .catch(error => {
            console.log(error);
          });
    }
  },
  computed: {
    starRating() {
      const rating = Math.round(this.model.fabrika.prosecnaOcena);
      return Array.from({ length: 5 }, (_, index) => index < rating);
    },
    commentStarRating() {
      return (rating) => {
        const stars = Math.round(rating);
        return Array.from({ length: 5 }, (_, index) => index < stars);
      };
    }
  }
};
</script>

<style>
#rating-stars {
  font-size: 1.5rem;
  color: gold;
}
</style>