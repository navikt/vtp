<template>
    <div class="datascenarier">
        <Scenariovelger></Scenariovelger>
        <hr/>
        <Scenarioliste></Scenarioliste>
    </div>
</template>

<script>
    // @ is an alias to /src
    import Scenariovelger from '@/components/Scenariovelger.vue'
    import Scenarioliste from '@/components/Scenarioliste.vue'

    export default {
        name: 'datascenarier',
        components: {
            Scenariovelger,
            Scenarioliste
        }
    }
</script>
