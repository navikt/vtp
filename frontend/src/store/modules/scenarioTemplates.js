import axios from 'axios';
import {EventBus} from '@/main.js'

const state = {
    availableTemplates: [],
    initializedScenarios: [],
    selectedTemplate: null,
    templatesLoaded: false,
    templatesError: null,
    templatesErrorDetail: null
}

const getters = {
    getTemplatesLoaded: state => {
        return state.templatesLoaded;
    },
    getTemplatesError: state => {
        return state.templatesError;
    },
    getTemplatesErrorDetail: state => {
        return state.templatesErrorDetail;
    },
    getAvailableTemplates: state => {
        return state.availableTemplates;
    },
    getFirstAvailableTemplate: state => {
        return state.availableTemplates[0];
    },
    getSelectedTemplate: state => {
        return state.selectedTemplate;
    },
    initializedScenarios: state => {
        return state.initializedScenarios;
    }
}


const mutations = {
    setTemplatesLoaded: (state, loadedState) => {
        state.templatesLoaded = loadedState;
    },
    setTemplatesError: (state, err) => {
        state.templatesError = err;
    },
    setTemplatesErrorDetail: (state, errorDetail) => {
        state.templatesErrorDetail = errorDetail;
    },
    setAvailableTemplates: (state, templates) => {
        state.availableTemplates = templates;

    },
    setSelectedTemplate: (state, template) => {
        state.selectedTemplate = template;
    },
    addInitializedScenario: (state, scenario) => {
        state.initializedScenarios.push(scenario)
    },
    setInitializedScenarios: (state, scenarios) => {
        state.initializedScenarios = scenarios.reverse();
    }
}

const actions = {
    loadAvailableTemplates: ({commit, rootGetters}) => {
        axios
        //.get(context.rootState.backendHost + context.rootState.apiPath + "/testscenario/templates")
            .get(rootGetters.getApiUrl + "/testscenario/templates")
            .then(response => {
                var constructedTemplates = [];
                response.data.forEach(function (data) {
                    constructedTemplates.push({value: data.key, text: data.navn});
                }, this);
                commit('setAvailableTemplates', constructedTemplates);

            }).then(() => {
            commit('setTemplatesLoaded', true);
            EventBus.$emit('templatesWereLoaded', true);

        }).catch(error => {
            commit('setTemplatesLoaded', true);
            commit('setTemplatesError', 'Noe gikk galt, kunne ikke laste templates.');
            commit('setTemplatesErrorDetail', error.toString());

            if (error.toString().includes("Network")) {
                commit('setTemplatesErrorDetail', this.getTemplatesErrorDetail() + "  - har du husket å starte backend?");
            }

        });
    },
    refreshScenarios: ({commit, rootGetters}) => {
        axios
            .get(rootGetters.getApiUrl + "/testscenario/initialiserte")
            .then(response => {
                commit('setInitializedScenarios', response.data);
                /*
                response.data.forEach(function (data) {
                    commit('addInitializedScenario', data)
                });
                */
            });
    },
    slettScenario: (context, scenarioId) => {
        axios
            .delete(context.rootGetters.getApiUrl + "/testscenario/slettscenario/" + scenarioId)
            .then( () => {
                context.dispatch('refreshScenarios');
            })
    }
}

export default {
    state,
    getters,
    mutations,
    actions
}