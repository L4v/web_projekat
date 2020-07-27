var loginVue = new Vue
({
    el: "#login",
    data:
    {
        user: {},
        errors: [],
        successMsg: "",
        loggedIn: false,
    },
    methods:
    {
        login: function()
        {
            // TODO(Jovan): Validation
            // TODO(Jovan): Cuvanje u memoriji preko refresh tokena, umesto u local

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
        },
        verify: function()
        {
            var jwt = localStorage.jwt;
            if(!jwt)
            {
                // NOTE(Jovan): Ako ne postoji jwt, ne pokusavaj login
                return;
            }
            axios.get("rest/test", {headers:{"Authorization": "Bearer " + jwt}})
                .then(response =>
                {
                    this.loggedIn = true;
                })
                .catch(response =>
                {
                    this.loggedIn = false;
                });
        },
    }
})
