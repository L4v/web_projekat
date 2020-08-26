<template>
	<!-- TODO(Jovan): Merge search and navigation -->
	<!-- NOTE(Jovan): Navigation bar -->
	<div id="app" class="content">

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


        <!-- NOTE(Jovan): Modal login dialog -->
        <!-- TODO(Jovan): Make route???
        <div id="login" class="modal-container" :class="{modal_active: modal_active}">
            <form id="loginForm">
                <h3>Login</h3>
                <div class="loginField floating-label">
                    <label for="username">Username</label>
                    <input 
                        @focus="$event.target.parentElement.classList.toggle('focused')"
                        @blur="$event.target.parentElement.classList.toggle('focused')"
                        type="text" 
                        name="username" 
                        required
                    />
                </div>
                <div class="loginField floating-label">
                    <label for="password">Password</label>
                    <input
                        @focus="$event.target.parentElement.classList.toggle('focused')"
                        @blur="$event.target.parentElement.classList.toggle('focused')"
                        type="text" 
                        name="password" 
                        required
                    />
                </div>
                <div class="loginButtons">
                    <button class="button-primary">Log in</button>
                    <button>Forgot password?</button>
                    <button @click="modal_active=false">Cancel</button>
                </div>
            </form>
        </div> -->
	</div>
</template>
