<template>
<div>

    <b-form >
        <b-form-group>
            <b-form-select id="scenarioalias" v-model="selected" :options="scenarioOptions"></b-form-select>
            <b-button class="ml-4 mt-3" variant="primary">Opprett</b-button>
        </b-form-group>

    </b-form>

</div>
</template>

<style>
    .lol {
        text-align: center;
        display: block;
        margin-left: auto;
        color:red;
        margin-right: auto;
    }
</style>

<script>
    import axios from 'axios';
    export default {

        data() {
            return {
                selected: null,
                scenarier: null,
                scenarioOptions: [
                    {value: null, text: 'Please select'},
                    {value: 'a', text: '45-mor-fødsel-1-barn-stillingsprosent-0'}
                ]
            }
        },
        mounted () {
            axios
                .get("http://localhost:8060/rest/api/testscenario/templates")
                .then(response => this.scenarier = response )
        }
    }
</script>
