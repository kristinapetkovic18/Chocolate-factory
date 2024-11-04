<template>
  <div class="container mt-5">
    <div class="card">
      <div class="card-header">
        <h4>Unesi kriterijume pretrage</h4>
      </div>
      <div class="card-body">
        <div class="mb-3">
          <label for="firstName">Ime*</label>
          <input type="text" v-model="user.firstName" class="form-control" id="firstName" name="firstName">
        </div>
        <div class="mb-3">
          <label for="lastName">Prezime*</label>
          <input type="text" v-model="user.lastName" class="form-control" id="lastName" name="lastName">
        </div>
        <div class="mb-3">
          <label for="username">Korisničko ime*</label>
          <input type="text" v-model="user.username" class="form-control" id="username" name="username">
        </div>

        <div class="mb-3">
          <button type="button" @click="search" class="btn btn-secondary">Pretraži</button>
        </div>

        <div>
          <h4>Filtriraj rezultate:</h4>

          <div class="mb-3">
            <!-- Radio buttons for selecting role -->
            <label>
              <input type="radio" v-model="user.role" value="3" /> Admin
            </label>
            <label>
              <input type="radio" v-model="user.role" value="2" /> Menadžer

              <label>
                <input type="radio" v-model="user.role" value="1" /> Radnik
              </label>
            </label>
            <label>
              <input type="radio" v-model="user.role" value="0" /> Kupac
            </label>

            <label>
              <input type="radio" v-model="user.role" value="5" /> Svi
            </label>
          </div>

          <div class="mb-3">
            <!-- Dropdown for customer type, disabled based on role -->
            <select v-model="user.customerTypeName" class="form-control" id="customerTypeName" name="customerTypeName" :disabled="isCustomerTypeDisabled">
              <option value="">Izaberite tip kupca</option>
              <option value="BRONZE">Bronzani</option>
              <option value="SILVER">Srebrni</option>
              <option value="GOLD">Zlatni</option>
              <option value="NONE">Basic</option>
            </select>
          </div>
        </div>

      </div>
    </div>
  </div>
  <div class="container">
    <div class="card">
      <div class="card-body">
        <table class="table table-bordered">
          <thead>
          <tr>
            <th @click="sortBy('firstName')" :class="{'sorted-header': currentSort === 'firstName'}">
              Ime<span v-if="currentSort === 'firstName'">{{ currentSortDir === 'asc' ? ' ▲' : ' ▼' }}</span>
            </th>
            <th @click="sortBy('lastName')" :class="{'sorted-header': currentSort === 'lastName'}">
              Prezime<span v-if="currentSort === 'lastName'">{{ currentSortDir === 'asc' ? ' ▲' : ' ▼' }}</span>
            </th>
            <th @click="sortBy('username')" :class="{'sorted-header': currentSort === 'username'}">
              Korisničko ime<span v-if="currentSort === 'username'">{{ currentSortDir === 'asc' ? ' ▲' : ' ▼' }}</span>
            </th>
            <th @click="sortBy('role')" :class="{'sorted-header': currentSort === 'role'}">
              Uloga<span v-if="currentSort === 'role'">{{ currentSortDir === 'asc' ? ' ▲' : ' ▼' }}</span>
            </th>
            <th @click="sortBy('bodovi')" :class="{'sorted-header': currentSort === 'bodovi'}">
              Bodovi<span v-if="currentSort === 'bodovi'">{{ currentSortDir === 'asc' ? ' ▲' : ' ▼' }}</span>
            </th>
            <th>Tip kupca</th>
          </tr>
          </thead>
          <tbody v-if="sortedUsers.length > 0">
          <tr v-for="(user, index) in sortedUsers" :key="index">
            <td>{{ user.firstName }}</td>
            <td>{{ user.lastName }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.role }}</td>
            <td>{{ user.bodovi }}</td>
            <td>{{ user.tipKupca.name }}</td>
          </tr>
          </tbody>
          <tbody v-else>
          <tr>
            <td colspan="5">Nema rezultata</td>
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
  name: 'UsersSearch',
  data() {
    return {
      user: {
        firstName: '',
        lastName: '',
        username: '',
        role: '',
        customerTypeName: ''
      },
      users: [],
      currentSort: 'ime',
      currentSortDir: 'asc',
      role: 5
    };
  },
  mounted() {
    this.getUsers();
    this.checkLogin();
  },
  methods: {
    async search() {
      /*try {
        const isEmpty = !this.user.firstName &&
            !this.user.lastName &&
            !this.user.username &&
            !this.user.role &&
            !this.user.customerTypeName;

        if (isEmpty) {
          await this.getUsers();
          return;
        }
        const params = {
          firstName: this.user.firstName,
          lastName: this.user.lastName,
          username: this.user.username,
          role: this.user.role,
          customerTypeName: this.user.customerTypeName
        };

        const response = await axios.get('http://localhost:8081/WebShopAppREST/rest/users/search', { params });
        this.users = response.data;
      } catch (error) {
        console.error('Error searching for users:', error);
      }*/
    //  this.sortedUsers()
    }
,
    checkLogin() {
      axios.get('http://localhost:8081/WebShopAppREST/rest/users/loggedUser')
          .then(response => {
            this.role = response.data.role;
          })
          .catch(error => {
            console.log(error);
          });
    },
    getUsers() {
      axios.get('http://localhost:8081/WebShopAppREST/rest/users/get')
          .then(res => {
            this.users = res.data;
          });
    },
    sortBy(key) {
      if (this.currentSort === key) {
        this.currentSortDir = this.currentSortDir === 'asc' ? 'desc' : 'asc';
      } else {
        this.currentSort = key;
        this.currentSortDir = 'asc';
      }
    }
  },
  computed: {
    sortedUsers() {
      let filteredUsers = this.users;

      if (this.user.role ==="4" || this.user.role ==="3" ||this.user.role ==="2" ||this.user.role ==="1" ||this.user.role ==="0") {
        filteredUsers = filteredUsers.filter(user => user.role === Number(this.user.role));
      } else {
        filteredUsers = this.users;
      }

      if (this.user.firstName) {
        filteredUsers = filteredUsers.filter(user => user.firstName.toLowerCase().includes(this.user.firstName.toLowerCase()));
      }
      if (this.user.lastName) {
        filteredUsers = filteredUsers.filter(user => user.lastName.toLowerCase().includes(this.user.lastName.toLowerCase()));
      }
      if (this.user.username) {
        filteredUsers = filteredUsers.filter(user => user.username.toLowerCase().includes(this.user.username.toLowerCase()));
      }
      if (this.user.role && this.user.role !== "5") {
        filteredUsers = filteredUsers.filter(user => user.role === Number(this.user.role));
      }
      if (this.user.customerTypeName) {
        filteredUsers = filteredUsers.filter(user => user.tipKupca.name === this.user.customerTypeName);
      }



      if (this.user.customerTypeName) {
        filteredUsers = filteredUsers.filter(user => user.tipKupca.name === this.user.customerTypeName);
      }


      return filteredUsers.slice().sort((a, b) => {
        let modifier = 1;
        if (this.currentSortDir === 'desc') modifier = -1;
        if (a[this.currentSort] < b[this.currentSort]) return -1 * modifier;
        if (a[this.currentSort] > b[this.currentSort]) return 1 * modifier;
        return 0;
      });
    },
    isCustomerTypeDisabled() {
      return this.user.role !== "0";
    }
  }
};
</script>

<style>
.sorted-header {
  font-weight: bold;
}
</style>
