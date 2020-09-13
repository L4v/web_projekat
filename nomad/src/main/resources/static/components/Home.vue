<template>
	<!-- TODO(Jovan): Merge search and navigation -->
	<!-- NOTE(Jovan): Navigation bar -->
	<div class="content">

        <main-navigation :user="user"></main-navigation>
		<!-- NOTE(Jovan): Main content -->
		<!-- NOTE(Jovan): Header -->
		<section class="header">
			<div class="header-content row">
				<!-- TODO(Jovan): Bolji slogan? Mozda generator? -->
				<div class="one column">&nbsp;</div>
				<div class="two columns">
					<div class="slogan">
						<div class="slogan-blur"></div>
						<h2>Be at home, anywhere!</h2>
						<p>Find yourself a home on your travels or offer other
							travellers a place to stay</p>
						<button class="button-primary">Find a home</button>
					</div>
				</div>
			</div>
		</section>

		<!-- NOTE(Jovan): Footer -->
		<section class="footer">
			<ul class="footer-list">
				<li class="footer-item"><a href="">About</a></li>
				<li class="footer-item"><a href="footer-item">Contact</a></li>
			</ul>
        </section>
	</div>
</template>
<script>
    module.exports =
    {
        data: function()
        {
            return{
                user: null,
            }
        },
        methods:
        {
            getUser: function()
            {
                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    // TODO(Jovan): handle?
                    return;
                }

                axios.get("rest/get_user", {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response => 
                    {
                        this.user = response.data;
                    })
                    .catch(response =>
                    {
                        // TODO(Jovan): Handle?
                    });
            },
        },
        mounted()
        {
            console.log("Mounted home");
            this.getUser();
        },
    }
</script>
<style scoped>
    .content
    {
	    min-height: 200vh;
    }
    .header {
        /* TODO(Jovan): Postaviti adekvatnu pozadinu*/
        background-color: #fff;
        background-image: url("../img/novisad.png");
        background-position: center;
        background-repeat: no-repeat;
        background-size: cover;
        min-height: 100vh;
        padding-top: 15vh;
    }

    .header-content {
        padding-top: 10vh;
    }

    .slogan {
        position: relative;
    }

    .slogan h2, .slogan p, .slogan button {
        position: relative;
        text-align: left;
        color: #fff;
        font-weight: 500;
        z-index: 1;
    }

    .slogan-blur {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        filter: blur(8rem);
        -webkit-filter: blur(8rem);
        background-color: rgba(0, 0, 0, .5);
        z-index: 0;
    }

    .header h1, h2 {
        text-align: center;
    }

    .header-item {
        display: inline;
    }

    .host-btn {
        background-color: #fff;
    }

    .fade-enter-active, .fade-leave-active {
        transition: opacity 500ms;
    }

    .fade-enter, .fade-leave-to {
        opacity: 0;
    }
</style>
