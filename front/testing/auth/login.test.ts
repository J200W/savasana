/**
 * Test pour le composant LoginComponent
 */

import {LoginComponent} from "../../src/app/features/auth/components/login/login.component";
import {SessionService} from "../../src/app/services/session.service";
import {expect} from '@jest/globals';
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatFormFieldModule} from "@angular/material/form-field";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientModule} from "@angular/common/http";
import {MatCardModule} from "@angular/material/card";
import {ReactiveFormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../../src/app/features/auth/services/auth.service";
import {ListComponent} from "../../src/app/features/sessions/components/list/list.component";
import {of, throwError} from "rxjs";
import {NgZone} from "@angular/core";


describe('Component: LoginComponent', () => {
    let component: LoginComponent;
    let fixture: ComponentFixture<LoginComponent>;
    let authService: AuthService;
    let router: Router;
    let spyRouter: jest.SpyInstance;
    let spySubmit: jest.SpyInstance;
    let ngZone: NgZone;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [LoginComponent],
            providers: [SessionService],
            imports: [
                RouterTestingModule.withRoutes([
                    {path: "sessions", component: ListComponent}
                ]),
                BrowserAnimationsModule,
                HttpClientModule,
                MatCardModule,
                MatIconModule,
                MatFormFieldModule,
                MatInputModule,
                ReactiveFormsModule
            ]
        })
            .compileComponents();
        fixture = TestBed.createComponent(LoginComponent);
        component = fixture.componentInstance;
        authService = TestBed.inject(AuthService);
        router = TestBed.inject(Router);
        ngZone = TestBed.inject(NgZone);
        spyRouter = jest.spyOn(router, 'navigate');
        spySubmit = jest.spyOn(component, 'submit');
        fixture.detectChanges();
    });

    afterEach(() => {
        localStorage.removeItem('token');
    });

    it('should be able to submit the form', () => {
        component.submit();
        expect(spySubmit).toHaveBeenCalled();
    });

    it('should submit successfully', () => {
        const loginRequest = {
            email: 'yoga@studio.com',
            password: 'test!1234'
        };

        const mockResponse = {
            token: 'testToken',
            type: 'Bearer',
            id: 1,
            username: "Test user",
            firstName: "Test first name",
            lastName: "Test last name",
            admin: false
        }

        const spyLogin =
            jest.spyOn(authService, 'login').mockReturnValue(of(mockResponse));

        component.form.setValue(loginRequest);

        ngZone.run(() => {
            component.submit();
        });

        expect(spySubmit).toHaveBeenCalled();
        expect(spyLogin).toHaveBeenCalled();
        expect(component.onError).toBe(false);
        expect(spyRouter).toHaveBeenCalledWith(['/sessions']);
    });

    it('shouldn\'t submit with an error', () => {
        const loginRequest = {
            email: 'nimportequoi@gmail.com',
            password: 'test!1234'
        };

        jest.spyOn(authService, 'login').mockImplementation(() => {
            return throwError(() => new Error('Error'));
        });

        const spy = jest.spyOn(component, 'submit');
        component.form.setValue(loginRequest);
        component.submit();

        expect(spySubmit).toHaveBeenCalled();
        expect(component.onError).toBe(true);
    });
});
