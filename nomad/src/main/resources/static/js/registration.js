var regVue = new Vue
({
    el: "#registrationForm",
    data:
    {
        guest: {},
        successMsg: "",
        exists: false,
    },
    methods:
    {
        registerGuest: function(guest)
        {
            let g = 
                {
                    username: guest.username,
                    password: guest.password,
                    name:     guest.name,
                    surname:  guest.surname,
                    sex:      guest.sex,
                    userType: "GUEST",
                };
            const vm = this;
            axios.post("rest/reg_guest", g)
            .then(response =>
                {
                    this.successMsg = "Registered";
                })
            .catch(error =>
                {
                    this.successMsg = "Already exists!";
                    this.exists = true;
                });
        }
    }
})
