var loginVue = new Vue
({
    el: "#login",
    data:
    {
        user: {},
        errors: [],
        successMsg: "",
    },
    methods:
    {
        login: function()
        {
            // TODO(Jovan): Validation

            axios.post("rest/login", this.user)
            .then(response =>
                {
                    this.successMsg = "Logged in!";
                    localStorage.jwt = response.data;
                })
            .catch(response =>
                {
                    this.successMsg = "Failed to log in!";
                });
        }
    }
})
