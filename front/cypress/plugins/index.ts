/**
 * @type {Cypress.PluginConfig}
 */
export default (on, config) => {
    return require('@cypress/code-coverage/task')(on, config);
}

