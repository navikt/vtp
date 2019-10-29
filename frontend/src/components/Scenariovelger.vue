<template>
    <div>

        <b-form>
            <span v-if="!getTemplatesLoaded && !getTemplatesError">Laster...</span>
            <b-form-group v-if="getTemplatesLoaded && !getTemplatesError">
                <b-form-select id="scenarioalias" v-model="selected" :options="getAvailableTemplates">

                    <!--option v-for="template in getAvailableTemplates" v-bind:value="template.value">
                        {{ template.navn }}
                    </option-->

                </b-form-select>
                <b-button class="ml-4 mt-3" variant="primary" v-on:click="opprett()">Opprett</b-button>
            </b-form-group>

        </b-form>

        <div v-if="getTemplatesError">
            <span>{{getTemplatesError}}</span><br/>
            <span>{{getTemplatesErrorDetail}}</span>
        </div>

    </div>
</template>

<style>
</style>

<script>
    import axios from 'axios';
    import {mapGetters} from 'vuex';
    import {EventBus} from "../main";

    export default {
        data() {
            return {

            }
        },
        computed: {
            ...mapGetters([
                'getAvailableTemplates',
                'getTemplatesLoaded',
                'getTemplatesError',
                'getTemplatesErrorDetail',
                'getFirstAvailableTemplate',
                'getApiUrl'
            ]),
            selected: {
                get: function () {
                    if(this.getFirstAvailableTemplate && this.$store.getters.getSelectedTemplate == null) {
                       return this.getFirstAvailableTemplate.value;
                    } else if(this.$store.getters.getSelectedTemplate) {
                        return this.$store.getters.getSelectedTemplate;
                    } else {
                        return null;
                    }

                },
                set: function (val) {
                    this.$store.commit('setSelectedTemplate', val);
                }
            }
        },
        methods: {
            opprett() {
                axios
                    .post(this.getApiUrl + "/testscenarios/" + this.selected)
                    .then(() => {
                        this.$store.dispatch('refreshScenarios');
                    });
            }
        },
        mounted() {
            EventBus.$on('templatesWereLoaded', () => {
                console.log("Caught event templates loaded");
                //this.selected = this.$store.getters.getFirstAvailableTemplate.value;
            });
        }
    }
</script>
