<template>
    <div>

        <b-form >
            <span v-if="!loaded && !error">Laster...</span>
            <b-form-group v-if="loaded && !error">
                <b-form-select id="scenarioalias" v-model="selected" :options="scenarioOptions"></b-form-select>
                <b-button class="ml-4 mt-3" variant="primary" v-on:click="opprett()">Opprett</b-button>
            </b-form-group>

        </b-form>

        <div v-if="error">
            <span>{{error}}</span><br />
            <span>{{errorDetail}}</span>
        </div>

    </div>
</template>

<style>
    .lol {
        text-align: center;
        display: block;
        margin-left: auto;
        color: red;
        margin-right: auto;
    }
</style>

<script>
    import axios from 'axios';
    import { mapGetters } from 'vuex';

    export default {
        data() {
            return {
                selected: null,
                scenarier: null,
                scenarioOptions: [],
                loaded: false,
                error: null,
                errorDetail:null
            }
        },
        computed: {
            ...mapGetters([
                'getBackendHost',
                'getApiUrl'
            ]),
        },
        methods: {
            opprett() {
                axios
                    .post(this.getApiUrl + "/testscenario/" + this.selected)
                    .then(response => {
                        console.log(response);
                    })
            }
        },
        mounted() {
            axios
                .get(this.getApiUrl + "/testscenario/templates")
                .then(response => {
                    response.data.forEach(function (data) {
                        this.scenarioOptions.push({value: data.key, text: data.navn});
                    }, this)
                }).then(() => {
                this.selected = this.scenarioOptions[0].value;
                this.loaded = true;
            }).catch(error => {
                this.loaded = true;
                this.error = "Noe gikk galt, kunne ikke laste templates.";
                this.errorDetail = error.toString();

                if (error.toString().includes("Network")) {
                    this.errorDetail = this.errorDetail + " - har du husket å starte backend?"
                }

            });
        }
    }
</script>
