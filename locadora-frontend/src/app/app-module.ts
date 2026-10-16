import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { LOCALE_ID, NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { BrowserModule } from '@angular/platform-browser';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';

import { AppRoutingModule } from './app-routing-module';
import { authInterceptor } from './core/auth-interceptor';
import { App } from './app';
import { Home } from './home/home';
import { SharedModule } from './shared/shared-module';

registerLocaleData(localePt);

@NgModule({
  declarations: [App, Home],
  imports: [BrowserModule, AppRoutingModule, SharedModule, MatToolbarModule, MatMenuModule],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(withInterceptors([authInterceptor])),
    { provide: LOCALE_ID, useValue: 'pt-BR' },
  ],
  bootstrap: [App],
})
export class AppModule {}
