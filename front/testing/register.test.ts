/**
 * Testing for register page
 */

import {RegisterComponent} from "../src/app/features/auth/components/register/register.component";
import {ReactiveFormsModule} from "@angular/forms";
import {expect} from '@jest/globals';
import {ComponentFixture, TestBed, waitForAsync} from "@angular/core/testing";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientModule} from "@angular/common/http";
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {Router} from "@angular/router";
import {AuthService} from "../src/app/features/auth/services/auth.service";
import {throwError} from "rxjs";

describe('Component: RegisterComponent', () => {
    let component: RegisterComponent;
    let fixture: ComponentFixture<RegisterComponent>;
    let router: Router;
    let authService: AuthService;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [RegisterComponent],
            imports: [
                BrowserAnimationsModule,
                HttpClientModule,
                ReactiveFormsModule,
                MatCardModule,
                MatFormFieldModule,
                MatIconModule,
                MatInputModule
            ]
        })
            .compileComponents();

        fixture = TestBed.createComponent(RegisterComponent);
        component = fixture.componentInstance;
        router = TestBed.inject(Router);
        authService = TestBed.inject(AuthService);
        fixture.detectChanges();
    });

    it('should be able to submit the form', () => {
        const spy = jest.spyOn(component, 'submit');
        component.submit();
        expect(spy).toHaveBeenCalled();
    });

    it('should submit successfully', () => {
        const randomInt = Math.floor(Math.random() * 4000);

        const registerRequest = {
            email: 'test-' + randomInt + '@test.com',
            password: 'test!1234',
            firstName: 'Test',
            lastName: 'Test'
        };

        const spy = jest.spyOn(component, 'submit');
        const spyNavigate = jest.spyOn(router, 'navigate');

        component.form.setValue(registerRequest);
        component.submit();

        expect(spy).toHaveBeenCalled();
        expect(component.onError).toBe(false);
    });

    it('should submit with error', () => {
        const registerRequest = {
            email: 'nimportequoi@gmail.com',
            password: '',
            firstName: 'Test',
            lastName: 'Test'
        };

        jest.spyOn(authService, 'register').mockImplementation(() => {
            return throwError(() => new Error('Error'));
        });

        const spy = jest.spyOn(component, 'submit');
        component.form.setValue(registerRequest);
        component.submit();

        expect(spy).toHaveBeenCalled();
        expect(component.onError).toBe(true);
    });
});