<template>
  <div class="home">
    <Navbar></Navbar>
    <Start></Start>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Start from '@/components/Start.vue'; // @ is an alias to /src
import Navbar from '@/components/Navbar.vue';

@Component({
  components: {
    Start,
    Navbar,
  },
})
export default class Datascenarier extends Vue {}
</script>
