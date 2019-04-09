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
    }
}

const actions = {
    loadAvailableTemplates: (context) => {
        axios
            .get(context.rootState.backendHost + context.rootState.apiPath + "/testscenario/templates")
            .then(response => {
                var constructedTemplates = [];
                response.data.forEach(function (data) {
                    constructedTemplates.push({value: data.key, text: data.navn});
                }, this);
                context.commit('setAvailableTemplates', constructedTemplates);

            }).then(() => {
            context.commit('setTemplatesLoaded', true);
            EventBus.$emit('templatesWereLoaded', true);

        }).catch(error => {
            context.commit('setTemplatesLoaded', true);
            context.commit('setTemplatesError', 'Noe gikk galt, kunne ikke laste templates.');
            context.commit('setTemplatesErrorDetail', error.toString());

            if (error.toString().includes("Network")) {
                context.commit('setTemplatesErrorDetail', this.getTemplatesErrorDetail() + "  - har du husket å starte backend?");
            }

        });
    }
}

export default {
    state,
    getters,
    mutations,
    actions
}