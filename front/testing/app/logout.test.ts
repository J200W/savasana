import {TestBed} from "@angular/core/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {expect} from "@jest/globals";
import {HttpClientModule} from "@angular/common/http";
import {MatToolbarModule} from "@angular/material/toolbar";
import {AppComponent} from "../../src/app/app.component";
import {SessionService} from "../../src/app/services/session.service";
import {Router} from "@angular/router";
import {NgZone} from "@angular/core";
import {of} from "rxjs";

describe('Component: AppComponent', () => {
    let sessionService: SessionService;
    let router: Router;
    let spyRouter: jest.SpyInstance;
    let ngZone: NgZone;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [
                RouterTestingModule,
                HttpClientModule,
                MatToolbarModule
            ],
            declarations: [
                AppComponent
            ],
        }).compileComponents();

        sessionService = TestBed.inject(SessionService);
        router = TestBed.inject(Router);
        ngZone = TestBed.inject(NgZone);
        sessionService.sessionInformation = {
            id: 1,
            username: 'test username',
            firstName: 'test firstname',
            lastName: 'test lastname',
            token: 'test',
            type: 'Bearer',
            admin: true
        };
        spyRouter = jest.spyOn(router, 'navigate');
    });

    it('should logout successfully', () => {
        const fixture = TestBed.createComponent(AppComponent);
        const app = fixture.componentInstance;

        sessionService.$isLogged = jest.fn().mockReturnValue(of(false));

        ngZone.run(() => {
            app.logout();
        });

        expect(spyRouter).toHaveBeenCalledWith(['']);
        expect(sessionService.sessionInformation).toBeUndefined();

        app.$isLogged().subscribe((isLogged) => {
            expect(isLogged).toBe(false);
        });
    });
});
