<template>
    <div>

        <div class="scenario card">
            <div class="card-header">
                <span class="float-left">{{ scenariodata.templateNavn}}</span>
                <span class="float-right">slett</span>
            </div>
            <div class="card-body">
                <h4>Rådata</h4>
                <tree-view :data="scenariodata" max-depth="2"></tree-view>
            </div>
        </div>


    </div>

</template>

<style scoped>
    .scenario {
        margin-bottom:1em;
    }

    .card-body {
        text-align: left;
    }
</style>

<script>
    export default {
        props: [
            'scenariodata'
        ],

    }
</script>