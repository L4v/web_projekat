<template>
	<div id="navapp">
		<nav id="navbar">
			<div id="nav" :class="{sticky:active}">
				<div class="nav-container" :class="toggleNavClass()">
					<!-- NOTE(Jovan): Logo -->
					<div class="navbar-item logo">
						<a class="button" href="index.html">nomad</a>
					</div>
	
					<!-- NOTE(Jovan): Searchbar -->
					<transition name="fade"> <!-- NOTE(Jovan): Expanded searchbar -->
					<div v-if="!active" id="searchbar" :class="toggleSearchClass()">
						<div class="search-field">
	                           <!-- TODO(Jovan): Add floating label animation -->
							<label class="search-field-label" for="country">Country</label>
	                           <input class="search-field-input" type="text"
								placeholder="Country" name="country" >
						</div>
						<div class="search-field">
							<label>City</label> <input type="text" placeholder="Which city?"
								name="city">
						</div>
						<div class="search-field">
							<label>Pick dates</label>
	                           <v-date-picker mode="range" v-model="daterange" :input-props="{placeholder: 'Arrival and departure dates'}"/>
						</div>
						<div class="search-field search-btn" :class="toggleSearchClass()">
							<button class="button-primary">
								<i class="fa fa-search" aria-hidden="true"></i>
							</button>
						</div>
					</div>
	
					<!-- NOTE(Jovan): Collapsed, in navbar, searchbar -->
					<div v-else id="searchbar" :class="toggleSearchClass()">
						<div class="search-field search-btn" :class="toggleSearchClass()">
							<input type="text" placeholder="Search for a home">
							<button class="button-primary">
								<i class="fa fa-search" aria-hidden="true"></i>
							</button>
						</div>
					</div>
					</transition>
	
					<div class="navbar-dropdown">
						<button class="navdropbtn">
							<i class="fa fa-bars" aria-hidden="true"></i> <i
								class="fa fa-user-o" aria-hidden="true"></i>
						</button>
						<!-- TODO(Jovan): Make Log and Sign into dropdown like menus -->
						<ul class="navbar-list">
	                           <li><router-link to="/login">Log in</router-link></li>
							<li><a href="registration.html">Sign up</a></li>
	                           <!-- TODO(Jovan): Make computed? -->
	                           <li><button>Log out</button></li>
						</ul>
					</div>
				</div>
			</div>
		</nav>
	</div>
</template>
<script>
	module.exports = {
		data: function() 
		{
			return{
				active: false,
				daterange: null
			}
			
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
	}
</script>
<style scoped>
	.navbar {
		transition: 100ms;
		padding: 0px 25px 0px 25px;
	}
	
	.navbar-sticky {
		transition: 100ms;
		padding: 0px 25px 0px 25px;
	}
	
	#nav {
		transition: 150ms;
		height: 10vh;
		width: 100%;
		/*background-color: #fff;*/
		position: fixed;
		top: 0;
		z-index: 3;
	}
	
	/* NOTE(Jovan):
	 * Koristi se za sticky navbar
	 */
	#nav.sticky {
		transition: 150ms;
		background-color: #fff;
		box-shadow: 0px 1px 10px #999;
	}
	
	.navbar-list, .header-list, .footer-list {
		list-style-type: none;
	}
	
	.nav-container {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		align-items: center;
		justify-content: space-between;
	}
	
	.sticky>.nav-container {
		flex-wrap: nowrap;
	}
	
	.nav-container>* {
		min-height: 10vh;
		flex-basis: 50%;
	}
	
	.navbar-list {
		display: none;
		float: right;
		position: absolute;
		min-width: 160px;
		background: #fff;
		box-shadow: 0px 8px 16px 0px rgb(0, 0, 0, 0.2);
		z-index: 1;
		overflow: auto;
	}
	
	.navbar-dropdown:hover .navbar-list {
		display: block;
		top: 7vh;
	}
	
	.navbar-list a {
		float: none;
		color: black;
		text-decoration: none;
		display: block;
		text-align: center;
	}
	
	.navbar-item {
		margin: 0;
		padding: 0;
	}
	
	.logo {
		flex-grow: 1;
		display: flex;
		align-items: center;
	}
	
	.navbar-sticky>.logo {
		max-width: 13rem;
	}
	
	.nav-container {
		max-width: 80vw;
		margin: auto;
	}
	
	.navbar-dropdown {
		max-width: 8rem;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	#nav>.nav-container>.navbar-dropdown>.navdropbtn {
		font-size: 1.8rem;
		border: none;
		color: #fff;
		text-transform: capitalize;
		font-weight: 600;
	}
	
	#nav.sticky>.nav-container>.navbar-dropdown>.navdropbtn {
		font-size: 1.8rem;
		border: none;
		text-transform: capitalize;
		font-weight: 400;
		color: #000;
	}
	
	#nav>.nav-container>.logo a {
		font-size: 1.8rem;
		border: none;
		color: #fff;
		text-transform: uppercase;
		font-weight: 600;
	}
	
	.navbar-list a {
		font-size: 1.5rem;
		border: none;
		text-transform: capitalize;
		font-weight: 400;
		color: #000;
		padding: 5px;
	}
	
	.navbar-list a:hover {
		color: #ff5722;
	}
	
	#nav.sticky>.nav-container>.logo a {
		font-size: 1.8rem;
		border: none;
		color: #ff5722;
		text-transform: uppercase;
		font-weight: 600;
	}
</style>