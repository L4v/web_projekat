var regVue = new Vue
({
    el: "#registrationForm",
    data:
    {
        guest: {},
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
                    userType: "GUEST"
                };
            axios
            .post("rest/reg_guest", g);
        }
    }
})
