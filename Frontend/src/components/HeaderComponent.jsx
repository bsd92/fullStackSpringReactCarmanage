import React from 'react'
import { logout } from '../services/authService'

const HeaderComponent=()=> {
  return (
    <div>
        <header>
            <nav className="navbar navbar-dark bg-dark">
                <a className="navbar-brand" href='/cars'>Liste des voitures</a>
                <a className="navbar-brand" href='/login' onClick={logout}>Deconnexion</a>
            </nav>
        </header>
      
    </div>
  )
}

export default HeaderComponent
