import Vue from 'vue'
import Vuex from 'vuex'
import scenarioTemplates from './modules/scenarioTemplates.js'

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
      scenarioTemplates
    },
    state: {
        backendHost: '',
        apiPath: ''
    },
    getters: {
        getBackendHost: state => {
            return state.backendHost;
        },
        getApiUrl: state => {
            return state.backendHost + state.apiPath;
        }
    },
    mutations: {
        setBackendHost: (state, targetHost) => {
            state.backendHost = targetHost;
        },
        setApiPath: (state, apiPath) => {
            state.apiPath = apiPath;
        }

    },
    actions: {
        setBackendHost: (context) => {
            var targetHost = document.location.protocol + '//' + document.location.hostname;

            /*
                Under utvikling så kjøres yarn på frontend, med CORS enabled mot backend,
                derfor kan ikke port plukkes ut fra url når vi er på localhost.

                Når den kjøres i produksjon på en annen host enn localhost så kan port
                plukkes rett fra url.

             */
            if (document.location.hostname === 'localhost') {
                var port;
                if (document.location.protocol === 'https:') {
                    port = '8063'; //todo skift ut med https fra en propertyfil
                } else {
                    port = '8060'; //todo skift ut med https fra en propertyfil
                }
                targetHost = targetHost + ':' + port;

            } else {
                targetHost = targetHost + ':' + document.location.port;
            }
            context.commit('setBackendHost', targetHost);

        },
        setApiPath: (context, apiPath) => {
            context.commit('setApiPath', apiPath);
            context.dispatch('loadAvailableTemplates');
        }
    }
})
