<template>
  <div class="home">
    <Navbar></Navbar>
    <p>Her kommer støtte for søknader etterhvert</p>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Start from '@/components/Start.vue'; // @ is an alias to /src
import Navbar from '@/components/Navbar.vue';

@Component({
  components: {
    Navbar,
  },
})
export default class Soknader extends Vue {}
</script>
