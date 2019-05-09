<template>
    <div>
        <Scenario v-for="scenario in initializedScenarios" :scenariodata="scenario" :key="scenario.id"></Scenario>
    </div>
</template>

<script>
    import {mapGetters} from 'vuex';
    import Scenario from '@/components/Scenario.vue'
    export default {
        name: 'scenario',
        data() {
          return {

          }
        },
        computed: {
            ...mapGetters([
                'initializedScenarios'
            ])
        },
        components: {
            Scenario
        }

    }
</script>