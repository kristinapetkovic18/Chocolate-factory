<template>
  <div class="container">

    <div v-if="sortedFilteredKupovine.length === 0" class="alert alert-info">
      No kupovine found.
    </div>

    <div class="mb-3">
      <label for="sortOptions" class="form-label">Sort by:</label>
      <select id="sortOptions" v-model="sortCriterion" class="form-select">
        <option value="cenaAsc">Cena < </option>
        <option value="cenaDesc">Cena > </option>
        <option value="datumAsc">Datum < </option>
        <option value="datumDesc">Datum > </option>
        <option   v-if="!loggedManager" value="fabrikaImeAsc">Fabrika Ime < </option>
        <option  v-if="!loggedManager" value="fabrikaImeDesc">Fabrika Ime > </option>
      </select>
    </div>
    <div class="mb-3">
      <form @submit.prevent="searchKupovine">
        <div class="mb-3">
          <label for="minCena" class="form-label">Min Cena:</label>
          <input type="number" id="minCena" v-model.number="searchParams.minCena" class="form-control" />
        </div>
        <div class="mb-3">
          <label for="maxCena" class="form-label">Max Cena:</label>
          <input type="number" id="maxCena" v-model.number="searchParams.maxCena" class="form-control" />
        </div>
        <div class="mb-3">
          <label for="startDatum" class="form-label">Start Datum:</label>
          <input type="date" id="startDatum" v-model="searchParams.startDatum" class="form-control" />
        </div>
        <div class="mb-3">
          <label for="endDatum" class="form-label">End Datum:</label>
          <input type="date" id="endDatum" v-model="searchParams.endDatum" class="form-control" />
        </div>
      </form>
    </div>

    <div v-for="kupovina in sortedFilteredKupovine" :key="kupovina.id" class="card mb-3">
      <div class="card-header">
        <h4>
          Kupovina {{ kupovina.id }}
          <button
                      class="btn btn-primary float-end"
                      v-if="!loggedManager && kupovina.status ==='Obrada' "
                      @click="cancelPurchase(kupovina.id, kupovina)"
          >Otkazi</button>

          <button  class="btn btn-success  mb-2 float-end"
                      @click="updatePurchaseStatus(kupovina.id, kupovina)"
                      v-if="loggedManager && kupovina.status ==='Obrada' ">Odobri</button>


          <button class="btn btn-danger  mb-2 float-end"
                  v-if="loggedManager && kupovina.status ==='Obrada' "
                  @click="showCommentField = !showCommentField">Odbij</button>

          <div v-if="showCommentField &&  kupovina.status ==='Obrada'">
            <textarea v-model="kupovina.porukaMenadzera" placeholder="Enter comment here" class="form-control mb-2"></textarea>
            <button v-if="loggedManager"
                    class="btn btn-danger" @click="decline(kupovina.id, { ...kupovina, status: 'Odbijeno', comment })">Submit</button>
          </div>

        </h4>
        <p><strong>Kupac ID:</strong> {{ kupovina.kupacId }}</p>
        <p><strong>Ime i Prezime kupca:</strong> {{ kupovina.imeIprezimeKupca }}</p>
        <p><strong>Cena:</strong> {{ kupovina.cena }}</p>
        <p><strong>Vreme Kupovine:</strong> {{ formatVremeKupovine(kupovina.vremeKupovine) }}</p>
        <p>Status:<b> {{kupovina.status }}</b></p>
        <div  v-if="kupovina.status === 'Odbijeno'">
          <p><strong>Komentar menadzera:</strong> {{ kupovina.porukaMenadzera}}</p>
        </div>

        <!-- Comment Section for Approved Purchases -->
        <div v-if="kupovina.status === 'Odobreno' && !loggedManager">
          <div class="mb-2">
            <label for="comment" class="form-label">Comment:</label>
            <input type="text" id="comment" v-model="comment" class="form-control" />
          </div>
          <div class="mb-2">
            <label for="rating" class="form-label">Rating:</label>
            <div class="rating">
              <span
                  v-for="star in 5"
                  :key="star"
                  @click="setRating(star)"
                  @mouseover="hoverRating(star)"
                  @mouseleave="hoverRating(null)"
                  :class="{ 'filled': star <= (hoveredRating || rating) }"
              >
                ★
              </span>
            </div>
          </div>
          <button class="btn btn-primary" @click="createComment(kupovina.kupacId, kupovina.id, kupovina.fabrikaId, comment)">Add Comment</button>
      </div> </div>
      <div class="card-body">
        <div v-if="!artikli[kupovina.id]" class="alert alert-info">
          Loading artikli...
        </div>
        <div v-else-if="artikli[kupovina.id].length === 0" class="alert alert-warning">
          No artikli found for this kupovina.
        </div>
        <table v-else class="table table-bordered">
          <thead>
          <tr>
            <th v-for="header in tableHeaders" :key="header">{{ header }}</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="artikal in artikli[kupovina.id]" :key="artikal.id">
            <td>{{ artikal.id }}</td>
            <td>{{ artikal.kupovinaId }}</td>
            <td>{{ artikal.cokoladaId }}</td>
            <td>{{ artikal.kolicina }}</td>
            <td>{{ artikal.fabrikaId }}</td>
            <td>{{ artikal.fabrikaIme }}</td>
            <td>{{ artikal.cokoladaIme }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

export default {
  name: "Kupovine",

  setup() {
    const loggedManager = ref(false); // Add this ref
    const showCommentField = ref(false);
    const porukaMenadzera = ref(''); // Store comment text
    const comment = ref(''); // Store comment text
    const username = ref(''); // Store comment text
    const id = ref(''); // Store comment text
    const kupovine = ref([]);
    const role = ref(5); // Store rating value
    const kupacId = ref(''); // Store comment text
    const kupacBodovi = ref(0); // Store comment text
    const rating = ref(0); // Store rating value
    const hoveredRating = ref(null); // Store hovered rating value
    const unfilteredKupovine = ref([]);
    const artikli = ref({});
    const error = ref(null);
    const sortCriterion = ref('cenaAsc'); // Default sorting criterion
    const searchParams = ref({
      minCena: null,
      maxCena: null,
      startDatum: '',
      endDatum: ''
    });
    const tableHeaders = ['ID', 'Kupovina ID', 'Cokolada ID', 'Količina', 'Fabrika ID', 'Ime fabrike', 'Cokolada Ime'];
    const statusLabels = {
      0: 'PENDING',
      1: 'COMPLETED',
      2: 'CANCELLED'
    };
    const fetchKupovine = async () => {
      try {
        const loggedUserId = localStorage.getItem('id');
        const userRole = localStorage.getItem('userRole');
        let res;

        if (userRole === '0') {
          res = await axios.get(`http://localhost:8081/WebShopAppREST/rest/kupovina/kupac/${loggedUserId}`);
          console.log('Kupovine for customer:', res.data);
          loggedManager.value = false;
        } else {
          loggedManager.value = true;
          res = await axios.get(`http://localhost:8081/WebShopAppREST/rest/kupovina/kupovine/menadzer/${loggedUserId}`);
          console.log('Kupovine for manager:', res.data);
        }

        kupovine.value = res.data;
        unfilteredKupovine.value = res.data;
        await fetchArtikli();

      } catch (err) {
        console.error('Error fetching kupovine:', err);
        error.value = 'Failed to fetch kupovine. Please try again later.';
      }
    };
    const fetchArtikli = async () => {
      const requests = kupovine.value.map(kupovina =>
          axios.get(`http://localhost:8081/WebShopAppREST/rest/kupovina/artikli/${kupovina.id}`)
      );
      try {
        const responses = await Promise.all(requests);
        responses.forEach((res, index) => {
          console.log(`Artikli for kupovina ${kupovine.value[index].id}:`, res.data);
          const artikliArray = Array.isArray(res.data) ? res.data : Object.values(res.data);
          artikli.value[kupovine.value[index].id] = artikliArray;
        });
      } catch (err) {
        console.error('Error fetching artikli:', err);
        error.value = 'Failed to fetch artikli. Please try again later.';
      }
    };


    const formatVremeKupovine = (vremeKupovine) => {
      return new Date(vremeKupovine).toLocaleString();
    };

    const sortedFilteredKupovine = computed(() => {

      const isEmptySearch = Object.values(searchParams.value).every(param => param === null || param === '');

      if (isEmptySearch) {

        return unfilteredKupovine.value.sort((a, b) => {
          switch (sortCriterion.value) {
            case 'cenaAsc':
              return a.cena - b.cena;
            case 'cenaDesc':
              return b.cena - a.cena;
            case 'datumAsc':
              return new Date(a.vremeKupovine) - new Date(b.vremeKupovine);
            case 'datumDesc':
              return new Date(b.vremeKupovine) - new Date(a.vremeKupovine);
            case 'fabrikaImeAsc':
              return (artikli.value[a.id] && artikli.value[a.id][0]?.fabrikaIme || '').localeCompare(artikli.value[b.id] && artikli.value[b.id][0]?.fabrikaIme || '');
            case 'fabrikaImeDesc':
              return (artikli.value[b.id] && artikli.value[b.id][0]?.fabrikaIme || '').localeCompare(artikli.value[a.id] && artikli.value[a.id][0]?.fabrikaIme || '');
            default:
              return 0;
          }
        });
      }

      const filtered = unfilteredKupovine.value.filter(kupovina => {
        const { minCena, maxCena, startDatum, endDatum} = searchParams.value;
        const isWithinPriceRange = (minCena === null || kupovina.cena >= minCena) &&
            (maxCena === null || kupovina.cena <= maxCena);

        const isWithinDateRange = (!startDatum || new Date(kupovina.vremeKupovine) >= new Date(startDatum)) &&
            (!endDatum || new Date(kupovina.vremeKupovine) <= new Date(endDatum));
        return isWithinPriceRange && isWithinDateRange;
      });


      return filtered.sort((a, b) => {
        switch (sortCriterion.value) {
          case 'cenaAsc':
            return a.cena - b.cena;
          case 'cenaDesc':
            return b.cena - a.cena;
          case 'datumAsc':
            return new Date(a.vremeKupovine) - new Date(b.vremeKupovine);
          case 'datumDesc':
            return new Date(b.vremeKupovine) - new Date(a.vremeKupovine);
          case 'fabrikaImeAsc':
            return (artikli.value[a.id] && artikli.value[a.id][0]?.fabrikaIme || '').localeCompare(artikli.value[b.id] && artikli.value[b.id][0]?.fabrikaIme || '');
          case 'fabrikaImeDesc':
            return (artikli.value[b.id] && artikli.value[b.id][0]?.fabrikaIme || '').localeCompare(artikli.value[a.id] && artikli.value[a.id][0]?.fabrikaIme || '');
          default:
            return 0;
        }
      });
    });


    const searchKupovine = async () => {
      try {
        const { minCena, maxCena, startDatum, endDatum } = searchParams.value;

        const response = await axios.get('http://localhost:8081/WebShopAppREST/rest/kupovina/search', {
          params: {
            minCena,
            maxCena,
            startDatum,
            endDatum
          }
        });
        kupovine.value = response.data;
        unfilteredKupovine.value = response.data;
        fetchArtikli();
      } catch (err) {
        console.error('Error searching kupovine:', err);
        error.value = 'Failed to search kupovine. Please try again later.';
      }
    };

    const cancelPurchase = async (kupovinaId, kupovina) => {
      try {

        kupovina.status = 'Otkazano';
        const response = await axios.post(`http://localhost:8081/WebShopAppREST/rest/kupovina/update/${kupovinaId}`, kupovina);
        console.log('Update response:', response.data);
        const userId = kupovina.kupacId;
        const pointsToDeduct = calculatePoints(kupovina).toString();

        await axios.post(`http://localhost:8081/WebShopAppREST/rest/users/update/${userId}/kazneni/${pointsToDeduct}`);

      } catch (error) {
        console.error("Error canceling the purchase or updating points:", error);
      }
    };

    const calculatePoints = (kupovina) => {
      return Math.floor(kupovina.cena / 1000 * 133 * 4);
    };


    const updatePurchaseStatus = async (id, purchase) => {
      try {
        purchase.porukaMenadzera = porukaMenadzera;
        purchase.status = 'Odobreno';
        const response = await axios.post(`http://localhost:8081/WebShopAppREST/rest/kupovina/update/${id}`, purchase);
        console.log('Update response:', response.data);

        axios.post(`http://localhost:8081/WebShopAppREST/rest/users/update/${this.kupacId}/bodovi/${this.kupacBodovi}`)
            .then(response => {
              console.log('Update response:', response.data);
            })



        fetchKupovine();
      } catch (err) {
        console.error('Error updating purchase status:', err);
        error.value = 'Failed to update purchase status. Please try again later.';
      }
    };

    const decline  = async (id, purchase) => {
      try {
       // purchase.porukaMenadzera = porukaMenadzera;
        purchase.status = 'Odbijeno';

        const response = await axios.post(`http://localhost:8081/WebShopAppREST/rest/kupovina/update/${id}`, purchase);
        console.log('Update response:', response.data);
        fetchKupovine();
      } catch (err) {
        console.error('Error updating purchase status:', err);
        error.value = 'Failed to update purchase status. Please try again later.';
      }
    };

    onMounted(() => {
      checkLogin();
      fetchKupovine();
    });

    const setRating = (star) => {
      rating.value = star;
    };

    const hoverRating = (star) => {
      hoveredRating.value = star;
    };

    const createComment = async (userId, kupovinaId, fabrikaId, commentText) => {
     try {

       const commentData = {
          kupacId: userId,
          fabrikaId: fabrikaId,
          tekst: commentText,
          ocena:  rating.value
        };
        console.log(commentData)
      const response = await axios.post('http://localhost:8081/WebShopAppREST/rest/komentari/add', commentData);
        console.log('Comment created:', response.data);
      } catch (err) {
       console.error('Error creating comment:', err);
       error.value = 'Failed to create comment. Please try again later.';
     }
    };

    const checkLogin = () => {
      axios.get('http://localhost:8081/WebShopAppREST/rest/users/loggedUser')
          .then(response => {
            this.role  = response.data.role;
            this.id =  response.data.id;
            this.username = response.data.username;
            if (response.data && response.data.role === 2) {
              loggedManager.value = true;
            }
          })
          .catch(error => {
            console.log(error);
          });
    };

    return {
      kupovine,
      artikli,
      error,
      tableHeaders,
      sortCriterion,
      role,
      kupacId,
      kupacBodovi,
      formatVremeKupovine,
      sortedFilteredKupovine,
      searchParams, // Include searchParams in the return object
      searchKupovine,
      showCommentField,
      porukaMenadzera,
      comment,
      loggedManager,
      updatePurchaseStatus,
      decline,
      cancelPurchase,
      createComment,
      setRating,
      hoverRating,
      rating, // Expose rating
      hoveredRating, // Expose hoveredRating

    };
  }
};
</script>

<style scoped>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
.bold-text {
  font-weight: bold;
}
.rating {
  display: inline-flex;
  cursor: pointer;
}

.rating span {
  font-size: 24px;
  color: gray;
}

.rating span.filled {
  color: gold;
}

</style>
