<template>
    <div class="home">
        <Navbar></Navbar>
        <b-row>
            <b-col class="pb-2">

                <b-button size="sm"><font-awesome-icon icon="coffee"/></b-button>
            </b-col>
            <b-col class="pb-10">
                <h1>Hei</h1>
                <p>Hvorfor er alt i midten?</p>
            </b-col>
        </b-row>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import Start from '@/components/Start.vue'; // @ is an alias to /src
    import Navbar from '@/components/Navbar.vue';

    @Component({
        components: {
            Start,
            Navbar,
        },
    })
    export default class Datascenarier extends Vue {
    }
</script>
