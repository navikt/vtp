<template>
    <div>

        <b-form>
            <b-form-group>
                <b-form-select id="scenarioalias" v-model="selected" :options="scenarioOptions"></b-form-select>
                <b-button class="ml-4 mt-3" variant="primary">Opprett</b-button>
            </b-form-group>

        </b-form>

        <p>Backendhost: {{getBackendHost}}</p>
        <p>Api url: {{getApiUrl}}</p>

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
    import { mapActions } from 'vuex';

    export default {
        data() {
            return {
                selected: null,
                scenarier: null,
                scenarioOptions: []
            }
        },
        computed: {
            ...mapGetters([
                'getBackendHost',
                'getApiUrl'
            ]),
        },
        methods: {
            ...mapActions([
                'setBackendHost'
            ])
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
            });
        }
    }
</script>
