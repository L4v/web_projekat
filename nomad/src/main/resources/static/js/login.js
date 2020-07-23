var loginVue = new Vue
({
    el: "#login",
    data:
    {
        guest: {},
    },
    methods:
    {
        login: function(username, password)
        {
            //
            axios
            .post("rest/login", g);
        }
    }
})