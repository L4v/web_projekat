const routes =
[
    {
        path: "/",
        name: "Home",
        component: httpVueLoader("components/Home.vue")
    },
];

const router = new VueRouter({routes});

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
    router: router,
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
    },
    computed:
    {
        verifyLogin: function()
        {
            return this.verify();
        }
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
}).$mount("#app");
