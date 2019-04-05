<template>
    <div class="vtpnavbar">
        <b-navbar toggleable="lg" type="dark" variant="info">
            <b-navbar-brand href="#">VTP</b-navbar-brand>

            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav>
                    <b-nav-item active to="/">Datascenarioer</b-nav-item>
                    <b-nav-item to="/soknader">Søknader</b-nav-item>
                    <b-nav-item v-bind:href="swaggerUrl()" target="_blank">Swagger</b-nav-item>
                </b-navbar-nav>

            </b-collapse>
        </b-navbar>
    </div>
</template>

<style>
    .vtpnavbar {
        margin-bottom: 40px;
    }
</style>

<script>
    import {mapGetters} from 'vuex';

    export default {
        computed: {
            ...mapGetters([
                'getBackendHost'
            ])
        },
        methods: {
            swaggerUrl: function () {
                return this.getBackendHost + '/swagger';
            }
        }
    }
</script>
