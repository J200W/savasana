import { MeComponent } from '../../src/app/components/me/me.component';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { SessionService } from '../../src/app/services/session.service';
import { mount } from 'cypress/angular';
import { RouterTestingModule } from '@angular/router/testing';
import { UserService } from '../../src/app/services/user.service';
import { TestBed } from '@angular/core/testing';
import { AppComponent } from "../../src/app/app.component";
import { User } from '../../src/app/interfaces/user.interface';
// import "../../src/app/app.component.scss";
import "../../src/app/components/me/me.component.scss";

describe('MeComponent', () => {
    const mockSessionService = {
        sessionInformation: {
            admin: true,
            id: 1
        }
    };

    let mockUser: User = {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com',
        admin: false,
        password: 'password',
        createdAt: new Date(),
        updatedAt: new Date(),
    };

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [MeComponent],
            imports: [
                RouterTestingModule.withRoutes([
                    { path: "", component: AppComponent }
                ]),
                MatSnackBarModule,
                HttpClientTestingModule,
                MatCardModule,
                MatFormFieldModule,
                MatIconModule,
                MatInputModule
            ],
            providers: [{ provide: SessionService, useValue: mockSessionService }, UserService],
        }).compileComponents();
    });

    it('should mount and display user information', () => {
        // Set up the intercept before mounting the component
        // Mount the component
        mount(MeComponent, {
            imports: [
                MatButtonModule,
                MatCardModule,
                MatIconModule,
                MatToolbarModule,
                MatSnackBarModule,
                HttpClientTestingModule,
                MatFormFieldModule,
                MatInputModule
            ],
            providers: [{ provide: SessionService, useValue: mockSessionService }, UserService],
            componentProperties: {
                user: mockUser,
            }
        });

        // Wait for the HTTP request to be intercepted and verify the response
        // cy.wait('@getUser').then((interception) => {
        //     expect(interception.response.statusCode).to.equal(200);
        //     expect(interception.response.body).to.deep.equal(mockUser);
        // });


    });
});
