<template>
  <div class="container mt-5">
    <div class="card">
      <div class="card-header">
        <h4>Unesi kriterijume pretrage</h4>
      </div>
      <div class="card-body">
        <div class="mb-3">
          <label for="ime">Naziv fabrike*</label>
          <input type="text" v-model="searchCriteria.ime" class="form-control" id="ime" name="ime">
        </div>

        <div class="mb-3">
          <label for="minOcena">Minimalna ocena</label>
          <input type="number" v-model.number="searchCriteria.min" class="form-control" id="minOcena" name="minOcena" min="1" max="5">
        </div>
        <div class="mb-3">
          <label for="maxOcena">Maksimalna ocena</label>
          <input type="number" v-model.number="searchCriteria.max" class="form-control" id="maxOcena" name="maxOcena" min="1" max="5">
        </div>

        <div class="mb-3">
          <label for="l">Lokacija</label>
          <input type="text" v-model.number="searchCriteria.l" class="form-control" id="l" name="l">
        </div>
        <div class="mb-3">
          <label for="isFabrikaOpened">Samo otvorene fabrike</label>
          <input type="checkbox" v-model="searchCriteria.isFabrikaOpened" id="isFabrikaOpened" name="isFabrikaOpened">
        </div>
      </div>
    </div>
  </div>

  <div class="container mt-5">
    <div class="card">
      <div class="card-body">
        <table class="table table-bordered">
          <thead>
          <tr>
            <th>SLIKA</th>
            <th @click="sortBy('ime')" :class="{'sorted-header': currentSort === 'ime'}">
              IME
              <span v-if="currentSort === 'ime'">{{ currentSortDir === 'asc' ? ' ▲' : ' ▼' }}</span>
            </th>
            <th @click="sortBy('lokacija')" :class="{'sorted-header': currentSort === 'lokacija'}">
              LOKACIJA
              <span v-if="currentSort === 'lokacija'">{{ currentSortDir === 'asc' ? ' ▲' : ' ▼' }}</span>
            </th>
            <th @click="sortBy('prosecnaOcena')" :class="{'sorted-header': currentSort === 'prosecnaOcena'}">
              PROSECNA OCENA
              <span v-if="currentSort === 'prosecnaOcena'">{{ currentSortDir === 'asc' ? ' ▲' : ' ▼' }}</span>
            </th>
            <th></th>
          </tr>
          </thead>
          <tbody v-if="sortedFabrike.length > 0">
          <tr v-for="(fabrika, index) in sortedFabrike" :key="index">
            <td>
              <img :src="'../src/assets/pictures/' + fabrika.slika" alt="Slika fabrike" class="img-thumbnail" width="100">
            </td>
            <td>{{ fabrika.ime }}</td>
            <td>{{ fabrika.location?.address?.street || 'No location available' }}</td>
            <td>{{ fabrika.prosecnaOcena }}</td>
            <td>
              <RouterLink :to="{ path: '/' + fabrika.id }" class="btn btn-danger">
                PREGLED
              </RouterLink>
            </td>
          </tr>
          </tbody>
          <tbody v-else>
          <tr>
            <td colspan="5">No results found</td>
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
  name: 'FabrikePretraga',
  data() {
    return {
      searchCriteria: {
        ime: '',
        min: null,
        max: null,
        l:'',
        isFabrikaOpened: false
      },
      fabrike: [],
      currentSort: 'ime',
      currentSortDir: 'asc'
    };
  },
  mounted() {
    this.getFabrike();
  },
  methods: {
    getFabrike() {
      axios.get('http://localhost:8081/WebShopAppREST/rest/fabrika/sve')
          .then(res => {
            this.fabrike = res.data;
          })
          .catch(err => {
            console.error('Error fetching fabrike:', err);
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
    sortedFabrike() {
      let filtered = this.fabrike.filter(fabrika => {
        const matchesIme = fabrika.ime.toLowerCase().includes(this.searchCriteria.ime.toLowerCase());
        const matchesLokacija = fabrika.location?.address?.street.toLowerCase().includes(this.searchCriteria.l.toLowerCase())
        || fabrika.location?.address?.city.toLowerCase().includes(this.searchCriteria.l.toLowerCase())
        ;
        const matchesMin = this.searchCriteria.min != null ? fabrika.prosecnaOcena >= this.searchCriteria.min : true;
        const matchesMax = this.searchCriteria.max != null ? fabrika.prosecnaOcena <= this.searchCriteria.max : true;
        const matchesStatus = !this.searchCriteria.isFabrikaOpened || fabrika.status === true;
        return matchesIme && matchesMin && matchesLokacija && matchesMax && matchesStatus;
      });

      return filtered.slice().sort((a, b) => {
        let aValue = a[this.currentSort];
        let bValue = b[this.currentSort];

        if (this.currentSort === 'lokacija') {
          aValue = a.location?.address?.street || '';
          bValue = b.location?.address?.street || '';
        }

        let modifier = this.currentSortDir === 'asc' ? 1 : -1;
        if (aValue < bValue) return -1 * modifier;
        if (aValue > bValue) return 1 * modifier;
        return 0;
      });
    }
  }
};
</script>

<style scoped>
.sorted-header {
  cursor: pointer;
  background-color: #f0f0f0;
}
</style>
