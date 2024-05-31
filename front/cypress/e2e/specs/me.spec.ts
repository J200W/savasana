import '../../support/commands';
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { MeComponent } from "../../../src/app/components/me/me.component";
import { RouterTestingModule } from "@angular/router/testing";
import { AppComponent } from "../../../src/app/app.component";
import { MatSnackBar, MatSnackBarModule } from "@angular/material/snack-bar";
import { HttpClientModule } from "@angular/common/http";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { SessionService } from "../../../src/app/services/session.service";
import { UserService } from "../../../src/app/services/user.service";
import { DebugElement, NgZone } from "@angular/core";
import { Router } from "@angular/router";
import {mount} from "cypress/angular";

export default function meSpec() {
    describe('MeComponent', () => {

        let component: MeComponent;
        let fixture: ComponentFixture<MeComponent>;
        let debugElement: DebugElement;
        let router: Router;
        let userService: UserService;
        let sessionService: SessionService;
        let ngZone: NgZone;
        let matSnackBar: MatSnackBar;

        const mockSessionService = {
            sessionInformation: {
                admin: true,
                id: 1
            }
        }

        before(() => {

        });

        // JE SUIS ICI
        it('should display user information', () => {
            cy.login();
            const sessionService = new SessionService()
            sessionService.logIn({
                token: "string",
                type: "string",
                id: 1,
                username: "string",
                firstName: "string",
                lastName: "string",
                admin: true
            });
            cy.visit('/me');
        });
    });
}
