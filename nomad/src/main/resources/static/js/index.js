new Vue
({
    el: "#app",
    data:
    {
        active: false,
        selected_date: null,
        daterange: new Date(),
        modal_active: false,
        float_label: false,
    },
    methods:
    {
        toggleNavClass()
        {
            if(this.active == false)
            {
                return "navbar";
            }
            else
            {
                return "navbar-sticky";
            }
        },
        toggleSearchClass()
        {
            if(this.active == false)
            {
                return "";
            }
            else
            {
                return "search-collapse";
            }
        },
        logout: function()
        {
            let jwt = localStorage.jwt;
            if(jwt){
                localStorage.removeItem("jwt");
                alert("You have successfully logged out");
            } else {
                alert("You are not logged in");
            }
        },

    },
    mounted()
    {
        window.document.onscroll = () =>
        {
            let searchbar = document.getElementById("searchbar");
            if(window.scrollY > searchbar.offsetTop)
            {
                this.active = true;
            }
            else
            {
                this.active = false;
            }
        };
    }
});

